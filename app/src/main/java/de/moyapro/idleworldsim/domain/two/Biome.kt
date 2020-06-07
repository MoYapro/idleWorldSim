package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.consumption.FoodChain
import de.moyapro.idleworldsim.domain.consumption.FoodChainEdge
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
        val currentPopulation = populations
        val newPopulation = mutableMapOf<Species, Population>()
        foodChain.getRelations()
            .sortedBy { it.preference }
            .forEach { battle(it) }

        return emptyMap()
    }

    private fun battle(battleRelation: FoodChainEdge): Map<Species, Population> {
        val resultMap = mutableMapOf<Species, Population>()
        battleRelation.consumeFactor
        val producerPopulation = populations[battleRelation.producer] ?: Population(0.0)
        val consumerPopulation = populations[battleRelation.consumer] ?: Population(0.0)
        val resourcesAquiredByConsumer =
            battleRelation.producer.getEaten(
                producerPopulation,
                consumerPopulation,
                battleRelation.consumer
            )
        val consumerPopulationDifference: Population =
            battleRelation.consumer.grow(resourcesAquiredByConsumer) // this actually might increase consumer population

        return resultMap.toMap()
    }
}
