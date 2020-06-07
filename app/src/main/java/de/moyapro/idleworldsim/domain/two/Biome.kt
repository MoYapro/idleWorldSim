package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.consumption.FoodChain
import de.moyapro.idleworldsim.domain.valueObjects.Population

class Biome() {
    private val foodChain = FoodChain()
    private val populations: MutableMap<Species, Population> = mutableMapOf()


    fun process(): Biome {
        val changes = getPopulationChanges()
        populations += changes
        return this
    }

    fun settle(species: Species, population: Population = Population(1.0)): Biome {
        foodChain.add(species)
        populations[species] = population
        return this
    }

    operator fun get(species: Species): Population {
        return populations[species] ?: Population(0.0)
    }


    /**
     * Get difference in population per species. This should be the same changes as process but not applied to the biome
     */
    fun getPopulationChanges(): Map<Species, Population> {
        //     foodChain[soil].sortedBy { it.preference }.forEach { it.consumer.consume(it.producer) }
        return populations
            .map { (species, _) -> Pair(species, Population(0.0)) }
            .associate { it }
    }


}
