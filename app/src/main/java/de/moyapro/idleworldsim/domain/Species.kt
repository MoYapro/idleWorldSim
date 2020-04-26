package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Consumption
import de.moyapro.idleworldsim.domain.consumption.ResourceTypes.*
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.DeathRate
import de.moyapro.idleworldsim.domain.valueObjects.GrowthRate
import de.moyapro.idleworldsim.domain.valueObjects.HungerRate
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.util.applyTo

/**
 * Class representing a creature with some features consuming and producing resources according to those features
 */
class Species(val name: String, private val features: MutableSet<Feature> = mutableSetOf()) {
    constructor(name: String, feature: Feature) : this(name, mutableSetOf(feature))

    private val hungerRate = SpeciesConstants.HUNGER_RATE
    private val growthRate = features.applyTo(SpeciesConstants.GROWTH_RATE, Feature::influenceGrowthRate)


    private fun needsPerIndividual() = features.applyTo(Resources(DoubleArray(values().size) { 0.0 }), Feature::influenceNeed)

    fun getPopulationIn(biome: Biome): Population {
        return biome.resources.getPopulation(this)
    }

    fun process(totalSupplyFromBiome: Resources): Resources {
        val needs = needsPerIndividual() * (totalSupplyFromBiome.populations[this] ?: Population(1.0))
        val baseConsumption = Consumption(this, needs, totalSupplyFromBiome)
        val modifiedConsumption = features.applyTo(baseConsumption, Feature::influenceConsumption)
        return grow(modifiedConsumption)
    }

    fun evolve(vararg trait: Trait): Species {
        features.add(Feature(*trait))
        return this
    }

    fun evolve(vararg features: Feature): Species {
        this.features += features
        return this
    }

    private fun grow(consumption: Consumption): Resources {
        val provided = consumption.isProvided()
        return when {
            provided >= 1.0 -> {
                val leftovers = consumption.consume()
                leftovers.updatePopulation(consumption.consumer, this.growthRate)
                leftovers
            }
            provided < 0.8 -> consumption.supply.copy().updatePopulation(consumption.consumer, hungerRate)
            else -> {
                consumption.consume()
            }
        }
    }

    override fun toString(): String {
        return "Species[$name]"
    }

    fun hasTrait(trait: Trait): Boolean {
        return features.any { it.hasTrait(trait) }
    }

}

fun defaultSpecies(name: String = "DefaultSpecies"): Species {
    return Species(
        name, Feature(
            ConsumerTrait(Water),
            ConsumerTrait(Minerals),
            ConsumerTrait(Energy),
            NeedResource(Water),
            NeedResource(Minerals),
            NeedResource(Energy),
            ProduceResource(EvolutionPoints)
        )
    )
}


object SpeciesConstants {
    val GROWTH_RATE = GrowthRate(1.1)
    val DEATH_RATE = DeathRate(.95)
    val HUNGER_RATE = HungerRate(.5)
    val MINIMAL_POPULATION = Population(1E-6)
}
