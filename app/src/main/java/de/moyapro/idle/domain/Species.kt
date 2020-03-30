package de.moyapro.idle.domain

class Species(
    val name: String = "DefaultSpecies"
) {
    private val traits: MutableList<Trait> = mutableListOf()
    private fun needsPerIndividual() = Resources(-1.0, 1.0, 1.0, 1.0)

    fun getPopulationIn(biome: Biome): Double {
        return biome.resources.getPopulation(this)
    }

    fun process(totalSupplyFromBiome: Resources): Resources {
        val needs = needsPerIndividual() * (totalSupplyFromBiome.populations[this] ?: 0.0)
        val baseConsumption = Consumption(this, needs, totalSupplyFromBiome)
        val availableConsumption = traits.filterIsInstance<SupplyModifyingTrait>()
            .fold(baseConsumption)
            { modifiedConsumption, trait -> trait.influence(modifiedConsumption) }
        val modifiedConsumption = traits
            .fold(availableConsumption)
            { consumption, trait -> trait.influence(consumption) }
        return grow(modifiedConsumption)
    }

    fun evolve(trait: Trait): Species {
        this.traits.add(trait)
        return this
    }

    private fun grow(consumption: Consumption): Resources {
        val initialGrowthRate = 1.1
        val hungerRate = .95
        val modifiedGrowthRate = traits
            .filterIsInstance<GrowthModifyingTrait>()
            .fold(initialGrowthRate) { growthRate, trait -> trait.influenceGrowth(growthRate) }
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
