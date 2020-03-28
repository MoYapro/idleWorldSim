package de.moyapro.idle.domain

class Species(
    val name: String = "DefaultSpecies"
) {
    private val traits: MutableList<Trait> = mutableListOf()
    private fun needsPerIndividual() = Resources(-1.0, 1.0, 1.0, 1.0)

    fun getPopulationIn(biome: Biome): Double {
        return biome.resources.getPopulation(this)
    }

    fun process(supply: Resources): Resources {
        val needs = needsPerIndividual() * (supply.populations[this] ?: 0.0)
        val consumption = Consumption(this, needs, supply)
        traits.forEach { it.influence(consumption) }
        @Suppress("UnnecessaryVariable") // intentionaly to demonstrate meaning of return value // may be removed in the future
        val leftovers = grow(consumption)
        return leftovers
    }

    fun evolve(trait: Trait): Species {
        this.traits.add(trait)
        return this
    }

    private fun grow(consumption: Consumption): Resources {
        var growthRate = 1.1
        val hungerRate = .95
        traits.filterIsInstance<GrowthTrait>().forEach { growthRate = it.influence(growthRate) }
        return if (consumption.isProvided()) {
            val leftovers = consumption.consume()
            leftovers.updatePopulation(consumption.consumer, growthRate)
            leftovers
        } else
            consumption.supply.copy().updatePopulation(consumption.consumer, hungerRate)
    }
}
