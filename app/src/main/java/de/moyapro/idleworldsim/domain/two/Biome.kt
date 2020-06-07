package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.consumption.FoodChain
import de.moyapro.idleworldsim.domain.consumption.FoodChainEdge
import de.moyapro.idleworldsim.domain.consumption.Resources
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
        foodChain.getRelations()
            .sortedBy { it.consumerPreference }
            .forEach { battle(it) }

        return emptyMap()
    }

    /**
     * actual consumption process where producers are converted into resources for the consumer
     */
    private fun battle(battleRelation: FoodChainEdge): Map<Species, Population> {
        val resultMap = mutableMapOf<Species, Population>()
        battleRelation.consumeFactor
        val producerPopulation = populations[battleRelation.producer] ?: Population(0.0)
        val consumerPopulation = populations[battleRelation.consumer] ?: Population(0.0)
        val producerPopulationDifference: Population =
            battleRelation.producer.getEaten(
                producerPopulation,
                consumerPopulation,
                battleRelation.consumer,
                battleRelation.consumeFactor
            )
        val resourcesAquiredByConsumer: Resources =
            battleRelation.producer.getResourcesPerIndividuum() * producerPopulationDifference
        val consumerPopulationDifference: Population =
            battleRelation.consumer.grow(resourcesAquiredByConsumer)

        resultMap[battleRelation.consumer] = consumerPopulationDifference
        resultMap[battleRelation.producer] = producerPopulationDifference

        return resultMap.toMap()
    }
}
