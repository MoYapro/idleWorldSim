package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.FoodChain
import de.moyapro.idleworldsim.domain.consumption.FoodChainEdge
import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.PopulationChange
import java.util.*

class Biome(val name: String = "Biome", val id: UUID = UUID.randomUUID()) {
    val foodChain = FoodChain()
    private var lastPopulations: MutableMap<Species, Population> = mutableMapOf()
    private val populations: MutableMap<Species, Population> = mutableMapOf()
    private val biomeFeatures: MutableMap<BiomeFeature, Population> = mutableMapOf()
    var lastChanges: Map<TraitBearer, PopulationChange> = mapOf()


    fun process(): Biome {
        this.lastPopulations = HashMap(this.populations)
        val sortedByConsumerPreference = foodChain.getRelations()
            .sortedByDescending { it.consumerPreference }
        sortedByConsumerPreference
            .forEach { relation ->
                this.battle(relation)?.let { (producer, populationEaten) ->
                    if (producer is Species) {
                        this.populations[producer]?.plus(populationEaten).let {
                            this.populations.put(producer, it ?: Population(0))
                        }
                    }
                }
            }
        this.populations.forEach { (species, population) ->
            population.plus(species.grow(population))
        }
        return this
    }


    fun settle(species: Species, population: Population = Population(1.0)): Biome {
        val currentPopulation = populations[species] ?: Population(0.0)
        populations[species] = currentPopulation + population
        foodChain.add(species)
        return this
    }

    operator fun get(species: Species): Population {
        return populations[species] ?: Population(0.0)
    }

    fun getRelations(): List<FoodChainEdge> {
        return foodChain.getRelations()
    }

    /**
     * actual consumption process where producers are converted into resources for the consumer
     */
    private fun battle(battleRelation: FoodChainEdge): Pair<ResourceProducer, PopulationChange>? {
        val producerPopulation = populations[battleRelation.producer] ?: Population(1.0)
        val consumerPopulation = populations[battleRelation.consumer] ?: Population(0.0)
        val producerPopulationEaten: PopulationChange =
            battleRelation.producer.getEaten(
                producerPopulation,
                consumerPopulation,
                battleRelation.consumer,
                battleRelation.consumeFactor
            )
        val resourcesAquiredByConsumer: Resources =
            battleRelation.producer.getResourcesForConsumption(producerPopulationEaten)
        battleRelation.consumer.consume(consumerPopulation, resourcesAquiredByConsumer)

        return Pair(
            battleRelation.producer,
            producerPopulationEaten
        )
    }

    fun addResourceProducer(producer: ResourceProducer): Biome {
        foodChain.add(producer)
        return this
    }

    fun population(): Map<Species, Population> {
        return populations.map { Pair(it.key, it.value) }
            .associate { it }
    }

    fun place(biomeFeature: BiomeFeature, population: Population = Population(1.0)): Biome {
        foodChain.add(biomeFeature)
        biomeFeatures[biomeFeature] = population
        return this
    }

    fun species(): List<Species> {
        return this.population().map { it.key }
    }

    inline fun <reified T : TraitBearer> evolve(traitBearerToEvolve: T, newFeature: Feature): T {
        val newTraitBearer = traitBearerToEvolve.evolveTo(newFeature)
        when (newTraitBearer) {
            is Species -> settle(newTraitBearer)
            is ResourceProducer -> addResourceProducer(newTraitBearer)
        }
        return newTraitBearer
    }

    fun getLastPopulationChanges(): Map<Species, PopulationChange> {
        val result = this.populations
            .map { (species, population) ->
                Pair(
                    species,
                    PopulationChange(population.populationSize - (lastPopulations[species]?.populationSize ?: 0.0))
                )
            }.associate { it }
            .toMutableMap()
        result.putAll(
            lastPopulations
                .filterNot { this.populations.containsKey(it.key) }
                .map { (species, population) ->
                    Pair(species, PopulationChange(-population.populationSize))
                }
        )
        return result
    }
}
