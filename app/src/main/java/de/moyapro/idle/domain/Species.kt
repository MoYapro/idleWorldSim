package de.moyapro.idle.domain

class Species(
    val name: String = "DefaultSpecies"
) {
    companion object {
        fun needsPerIndividual() = Resources(-1.0, 1.0, 1.0, 1.0)
    }

    private val traits: MutableList<Trait> = mutableListOf()

    fun getPopulationIn(biome: Biome) : Double {
        return biome.resources.getPopulation(this)
    }

    fun process(resources: Resources) : Resources {
        val needs = needsPerIndividual() * (resources.populations[this] ?: 0.0)
        val consumption = Consumption (this, needs, resources)
        traits.forEach { it.influence(consumption) }
        return grow(consumption)
    }

    fun evolve(trait: Trait): Species {
        this.traits.add(trait)
        return this
    }

    private fun grow(consumption: Consumption): Resources {
        var growthRate = 1.1
        traits.forEach { if(it is GrowthTrait) growthRate = it.influence(growthRate) }
        return if (consumption.supply.canProvide(consumption.needs))
            consumption.supply.minus(consumption.needs)
                .setPopulation(this, consumption.getPopulation(this) * growthRate)
        else
            consumption.supply.copy().setPopulation(this, consumption.getPopulation(this) * 0.95)
    }
}
