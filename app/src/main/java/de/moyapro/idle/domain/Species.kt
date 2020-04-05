package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource
import de.moyapro.idle.domain.consumption.Resources
import de.moyapro.idle.domain.traits.ConsumerTrait
import de.moyapro.idle.domain.traits.Feature
import de.moyapro.idle.domain.traits.Trait
import de.moyapro.idle.util.applyTo

/**
 * Class representing a creature with some features consuming and producing resources according to those features
 */
class Species(val name: String, private val features: MutableSet<Feature> = mutableSetOf()) {
    constructor(name: String, feature: Feature) : this(name, mutableSetOf(feature))

    private fun needsPerIndividual() = Resources(doubleArrayOf(-1.0, 1.0, 1.0, 1.0))

    fun getPopulationIn(biome: Biome): Double {
        return biome.resources.getPopulation(this)
    }

    fun process(totalSupplyFromBiome: Resources): Resources {
        val needs = needsPerIndividual() * (totalSupplyFromBiome.populations[this] ?: 0.0)
        val baseConsumption = Consumption(this, needs, totalSupplyFromBiome)
        val modifiedConsumption = features.applyTo(baseConsumption, Feature::influenceConsumption)
        return grow(modifiedConsumption)
    }

    fun evolve(vararg trait: Trait): Species {
        features.add(Feature(*trait))
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

    fun getEffectiveTraits(): Set<Trait> {
        return features.fold(mutableSetOf()) { s, f -> (s + f.traits).toMutableSet() }
    }

}

fun defaultSpecies(name: String = "DefaultSpecies"): Species {
    return Species(
        name, Feature(
            ConsumerTrait(Resource.Water),
            ConsumerTrait(Resource.Minerals),
            ConsumerTrait(Resource.Energy)
        )
    )
}
