package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource.*
import de.moyapro.idle.domain.consumption.Resources
import de.moyapro.idle.domain.traits.*
import de.moyapro.idle.util.applyTo

/**
 * Class representing a creature with some features consuming and producing resources according to those features
 */
class Species(val name: String, private val features: MutableSet<Feature> = mutableSetOf()) {
    constructor(name: String, feature: Feature) : this(name, mutableSetOf(feature))

    private fun needsPerIndividual() = features.applyTo(Resources(DoubleArray(values().size) { 0.0 }), Feature::influenceNeed)

    fun getPopulationIn(biome: Biome): Double {
        return biome.resources.getPopulation(this)
    }

    fun process(totalSupplyFromBiome: Resources): Resources {
        val needs = needsPerIndividual() * (totalSupplyFromBiome.populations[this] ?: 1.0)
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
        val initialGrowthRate = 1.1
        val hungerRate = .95
        val modifiedGrowthRate = features.applyTo(initialGrowthRate, Feature::influenceGrowthRate)
        return if (consumption.isProvided()) {
            val leftovers = consumption.consume()
            leftovers.updatePopulation(consumption.consumer, modifiedGrowthRate)
            leftovers
        } else
            consumption.supply.copy().updatePopulation(consumption.consumer, hungerRate)
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
