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
        val neededUntilSatisfied = calculateNeededUntilSatisfied(consumer.currentNeed(consumerPopulation))
        return minOf(
            neededUntilSatisfied.abs(), maxEaten.abs()
        ) * -1
    }

    fun calculateNeededUntilSatisfied(
        needs: List<Resource>
    ): PopulationChange {
        val productionPerProducer = getResourcesPerInstance()
        return PopulationChange(
            ceil(needs
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
