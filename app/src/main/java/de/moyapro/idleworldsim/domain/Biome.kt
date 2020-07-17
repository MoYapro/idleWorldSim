package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.FoodChain
import de.moyapro.idleworldsim.domain.consumption.FoodChainEdge
import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.addPopulationMaps
import de.moyapro.idleworldsim.util.sumUsing

class Biome {
    private val foodChain = FoodChain()
    private val populations: MutableMap<TraitBearer, Population> = mutableMapOf()
    private val biomeFeatures: MutableMap<BiomeFeature, Population> = mutableMapOf()


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

    fun getRelations(): List<FoodChainEdge> {
        return foodChain.getRelations()
    }


    /**
     * Get difference in population per species. This should be the same changes as process but not applied to the biome
     */
    fun getPopulationChanges(): Map<TraitBearer, Population> {
        val populationChanges = foodChain.getRelations()
            .sortedBy { it.consumerPreference }
            .map { battle(it) }

        return populationChanges.sumUsing(::addPopulationMaps) { mutableMapOf() } ?: emptyMap()
    }

    /**
     * actual consumption process where producers are converted into resources for the consumer
     */
    private fun battle(battleRelation: FoodChainEdge): Map<TraitBearer, Population> {
        battleRelation.consumeFactor
        val producerPopulation = populations[battleRelation.producer] ?: Population(0.0)
        val consumerPopulation = populations[battleRelation.consumer] ?: Population(0.0)
        val producerPopulationEaten: Population =
            battleRelation.producer.getEaten(
                producerPopulation,
                consumerPopulation,
                battleRelation.consumer,
                battleRelation.consumeFactor
            )
        val resourcesAquiredByConsumer: Resources =
            battleRelation.producer.getResourcesPerInstance() * producerPopulationEaten
        val consumerPopulationDifference: Population =
            battleRelation.consumer.consume(consumerPopulation, resourcesAquiredByConsumer)

        return mapOf(
            Pair(battleRelation.consumer, consumerPopulationDifference),
            Pair(battleRelation.producer, producerPopulationEaten * -1)
        )
    }

    fun addResourceProducer(producer: ResourceProducer): Biome {
        foodChain.add(producer)
        return this
    }

    fun population(): Map<Species, Population> {
        return populations.filter { it.key is Species }.map { Pair(it.key as Species, it.value) }.associate { it }
    }

    fun place(biomeFeature: BiomeFeature, population: Population = Population(1.0)): Biome {
        foodChain.add(biomeFeature)
        biomeFeatures[biomeFeature] = population
        return this
    }

    fun species(): List<Species> {
        return this.population().map { it.key }
    }

    fun <T : TraitBearer> evolve(traitBearerToEvolve: T, newFeature: Feature): T {
        val newTraitBearer = traitBearerToEvolve.evolveTo(newFeature)
        when (newTraitBearer) {
            is Species -> settle(newTraitBearer)
            is ResourceProducer -> addResourceProducer(newTraitBearer)
        }
        return newTraitBearer
    }
}
