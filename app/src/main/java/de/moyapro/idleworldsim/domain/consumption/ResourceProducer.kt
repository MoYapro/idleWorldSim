package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.TraitBearer
import de.moyapro.idleworldsim.domain.traits.Size
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.PopulationChange
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import kotlin.math.ceil

interface ResourceProducer : TraitBearer {

    /**
     * The resourceProducer gets eaten!
     *
     * @return how many producers gets eaten
     */
    fun getEaten(
        producerPopulation: Population,
        consumerPopulation: Population,
        consumer: ResourceConsumer,
        consumeFactor: Double
    ): PopulationChange {
        val maxEaten = producerPopulation * consumeFactor * -1
        val neededUntilSatisfied = calculateNeededUntilSatisfied(consumer.needs(), consumerPopulation)

        return when {
            neededUntilSatisfied.abs() < maxEaten.abs() -> neededUntilSatisfied
            else -> maxEaten
        }
    }

    fun calculateNeededUntilSatisfied(
        needsPerIndivitual: List<Resource>,
        consumerPopulation: Population
    ): PopulationChange {
        val productionPerProducer = getResourcesPerInstance()
        return PopulationChange(
            ceil(needsPerIndivitual.map { resourceNeededByIndividual -> resourceNeededByIndividual * consumerPopulation }
                .map { neededResource -> neededResource / productionPerProducer[neededResource.resourceType] }
                .max() // use highest resource as base to ensure consumer gets what it needs
                ?: 0.0
            )
                    * -1 // eating removed population -> negative change value
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getResourcesPerInstance(): Resources =
        traits()
            .map { it.getConsumptionResources(this[Size::class].firstOrNull()) }
            .reduceOrNull { resources1, resources2 -> resources1 + resources2 }
            ?: emptyResources()

    fun getResourcesForConsumption(producerPopulationEaten: PopulationChange): Resources =
        getResourcesPerInstance() * (producerPopulationEaten * -1)
}
