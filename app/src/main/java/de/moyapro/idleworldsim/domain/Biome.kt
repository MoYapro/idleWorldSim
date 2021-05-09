package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.FoodChain
import de.moyapro.idleworldsim.domain.consumption.FoodChainEdge
import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.PopulationChange
import de.moyapro.idleworldsim.domain.valueObjects.removeUnchanged
import de.moyapro.idleworldsim.util.sumUsing
import java.util.*

class Biome(val name: String = "Biome", val id: UUID = UUID.randomUUID()) {
    val foodChain = FoodChain()
    private val populations: MutableMap<TraitBearer, Population> = mutableMapOf()
    private val biomeFeatures: MutableMap<BiomeFeature, Population> = mutableMapOf()
    var lastChanges: Map<TraitBearer, PopulationChange> = mapOf()


    fun process(): Biome {
        val changes = getPopulationChanges()
        lastChanges = changes
        updatePopulations(populations, changes)
        return this
    }

    private fun updatePopulations(
        populations: MutableMap<TraitBearer, Population>,
        changes: Map<TraitBearer, PopulationChange>
    ) {
        changes.forEach { (traitBearer, populationChange) ->
            val oldPopulation = populations.getOrDefault(traitBearer, Population(0))
            val newPopulation =
                oldPopulation.plus(populationChange)
            populations[traitBearer] = newPopulation
        }
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

    fun getFoodChainGraph() = foodChain.generateGraph()


    /**
     * Get difference in population per species. This should be the same changes as process but not applied to the biome
     */
    fun getPopulationChanges(): Map<TraitBearer, PopulationChange> {
        val sortedByDescending = foodChain.getRelations()
            .sortedByDescending { it.consumerPreference }
        val populationEaten: Map<TraitBearer, PopulationChange> = sortedByDescending
            .map { battle(it) }
            .sumUsing({ t1, t2 -> t1 + t2 }, { mutableMapOf() })
            ?: emptyMap()

        val populationGrown: Map<TraitBearer, PopulationChange> = species()
            .associateBy({ species -> species }, { species -> species.grow(this[species]) })
        val allChanges: Map<TraitBearer, PopulationChange> = populationGrown + populationEaten
        return allChanges.removeUnchanged()

    }

    /**
     * actual consumption process where producers are converted into resources for the consumer
     */
    private fun battle(battleRelation: FoodChainEdge): Map<TraitBearer, PopulationChange> {
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
            battleRelation.producer.getResourcesPerInstance() * producerPopulationEaten
        battleRelation.consumer.consume(consumerPopulation, resourcesAquiredByConsumer)
        return mapOf(
            Pair(
                battleRelation.producer,
                producerPopulationEaten
            )
        )
    }

    fun addResourceProducer(producer: ResourceProducer): Biome {
        foodChain.add(producer)
        return this
    }

    fun population(): Map<Species, Population> {
        return populations.filter { it.key is Species }.map { Pair(it.key as Species, it.value) }
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
}
