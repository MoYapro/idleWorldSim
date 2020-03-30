package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource
import de.moyapro.idle.domain.consumption.Resources
import de.moyapro.idle.domain.traits.*

class Species(
    val name: String,
    private val traits: MutableList<Trait> = mutableListOf()
) {
    private fun needsPerIndividual() = Resources(doubleArrayOf(-1.0, 1.0, 1.0, 1.0))

    fun getPopulationIn(biome: Biome): Double {
        return biome.resources.getPopulation(this)
    }

    fun process(totalSupplyFromBiome: Resources): Resources {
        val needs = needsPerIndividual() * (totalSupplyFromBiome.populations[this] ?: 0.0)
        val baseConsumption = Consumption(this, needs, totalSupplyFromBiome)
        val availableConsumption = traits.applyTo(baseConsumption, SupplyModifyingTrait::influence)
        val modifiedConsumption = traits.applyTo(availableConsumption, ConsumptionModifyingTrait::influence)
        return grow(modifiedConsumption)
    }

    fun evolve(vararg trait: Trait): Species {
        this.traits.addAll(trait)
        return this
    }

    private fun grow(consumption: Consumption): Resources {
        val initialGrowthRate = 1.1
        val hungerRate = .95
        val modifiedGrowthRate = traits.applyTo(initialGrowthRate, GrowthModifyingTrait::influenceGrowth)
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

}

fun defaultSpecies(name: String = "DefaultSpecies"): Species {
    return Species(
        name, mutableListOf(
            ConsumerTrait(Resource.Water),
            ConsumerTrait(Resource.Minerals),
            ConsumerTrait(Resource.Energy)
        )
    )
}
