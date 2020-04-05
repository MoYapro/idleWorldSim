package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource
import de.moyapro.idle.domain.consumption.Resources
import de.moyapro.idle.domain.traits.*
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
        val modifiedConsumption = getEffectiveTraits().applyTo(baseConsumption, ConsumptionModifyingTrait::influence)
        return grow(modifiedConsumption)
    }

    fun evolve(vararg trait: Trait): Species {
        features.add(Feature(*trait))
        return this
    }

    fun evolve(vararg newFeatures: Feature): Species {
        features.addAll(newFeatures)
        return this
    }


    private fun grow(consumption: Consumption): Resources {
        val initialGrowthRate = 1.1
        val hungerRate = .95
        val modifiedGrowthRate = getEffectiveTraits().applyTo(initialGrowthRate, GrowthModifyingTrait::influenceGrowth)
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
        val activeTraitSet = ActiveTraitSet()
        features.forEach { activeTraitSet.addTraitsFrom(it) }
        return activeTraitSet.inclued
    }

}

class ActiveTraitSet(
    val inclued: MutableSet<Trait> = mutableSetOf(),
    val excluded: MutableSet<Trait> = mutableSetOf()
) {
    fun addTraitsFrom(feature: Feature) {
        excluded += feature.excludedTraits
        inclued += feature.traits
        inclued -= excluded
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
