package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Consumption
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.Trait
import de.moyapro.idleworldsim.domain.valueObjects.*
import de.moyapro.idleworldsim.util.applyTo

/**
 * Class representing a creature with some features consuming and producing resources according to those features
 */
class Species(val name: String, private val features: MutableSet<Feature> = mutableSetOf()) {
    constructor(name: String, feature: Feature) : this(name, mutableSetOf(feature))

    private fun hungerRate() = features.applyTo(SpeciesConstants.HUNGER_RATE, Feature::influenceHungerRate)
    private fun growthRate() = features.applyTo(SpeciesConstants.GROWTH_RATE, Feature::influenceGrowthRate)
    private fun deathRate() = features.applyTo(SpeciesConstants.DEATH_RATE, Feature::influenceDyingRate)


    fun needsPerIndividual() = features.applyTo(Resources(mutableMapOf()), Feature::influenceNeed)

    fun getPopulationIn(biome: Biome): Population {
        return biome.resources[this]
    }

    fun process(totalSupplyFromBiome: Resources): Resources {
        val needs = needsPerIndividual() * (totalSupplyFromBiome.populations[this] ?: Population(1.0))
        val baseConsumption = Consumption(this, needs, totalSupplyFromBiome)
        val modifiedConsumption = features.applyTo(baseConsumption, Feature::influenceConsumption)
        return die(grow(modifiedConsumption))
    }


    fun evolve(vararg trait: Trait): Species {
        features.add(Feature(*trait))
        return this
    }

    fun evolve(vararg features: Feature): Species {
        this.features += features
        return this
    }

    private fun die(resources: Resources) = resources.updatePopulation(this, this.deathRate())

    private fun grow(consumption: Consumption): Resources {
        val provided = consumption.isProvided()
        return when {
            provided >= 1.0 -> {
                val leftovers = consumption.consume()
                leftovers.updatePopulation(consumption.consumer, this.growthRate())
                leftovers
            }
            provided >= 0.8 -> consumption.consume()
            else -> {
                consumption.supply.copy().updatePopulation(this, hungerRate())
            }
        }
    }

    override fun toString(): String {
        return "Species[$name | grow=${growthRate()}, die=${deathRate()}]"
    }

    fun hasTrait(trait: Trait): Boolean {
        return features.any { it.hasTrait(trait) }
    }

    override fun equals(other: Any?): Boolean {
        if (null == other || other !is Species) {
            return false
        }

        val nameEqual = name == other.name
        val featureCountEqual = features.size == other.features.size
        val featuresEqual = features.containsAll(other.features)
        return nameEqual && featureCountEqual && featuresEqual
    }

    override fun hashCode(): Int {
        return name.hashCode() * 31 + features.sumBy { it.hashCode() * 5 }
    }

    operator fun get(trait: Trait): Level {
        return features
            .map { it[trait] }
            .maxBy { it.level }
            ?: Level(0)
    }

}

object SpeciesConstants {
    val GROWTH_RATE = GrowthRate(1.1)
    val DEATH_RATE = DeathRate(1.0)
    val HUNGER_RATE = StarvationRate(.5)
    val MINIMAL_POPULATION = Population(1E-6)
}
