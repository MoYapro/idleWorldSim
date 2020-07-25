package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.TraitBearer
import de.moyapro.idleworldsim.domain.traits.Size
import de.moyapro.idleworldsim.domain.valueObjects.Population

interface ResourceProducer : TraitBearer {

    /**
     * The resourceProducer gets eaten!
     *
     * @return how many producers gets eaten
     */
    fun getEaten(producerPopulation: Population, consumerPopulation: Population, consumer: ResourceConsumer, consumeFactor: Double): Population {
        val productionPerProducer = getResourcesPerInstance()
        val maxEaten = producerPopulation * consumeFactor
        val neededUntilSatisfied = Population(
            consumer.needs()
                .map { 1 / (it / productionPerProducer[it.resourceType]) }
                .max() ?: 0.0
        )

        return when {
            neededUntilSatisfied < maxEaten -> neededUntilSatisfied
            else -> maxEaten
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getResourcesPerInstance(): Resources =
        traits()
            .map { it.getConsumptionResources(this[Size::class].firstOrNull()) }
            .reduceOrNull { resources1, resources2 -> resources1 + resources2 }
            ?: emptyResources()
}
