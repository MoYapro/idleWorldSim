package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.TraitBearer
import de.moyapro.idleworldsim.domain.valueObjects.Population

interface ResourceProducer  : TraitBearer {

    /**
     * The resourceProducer gets eaten!
     *
     * @return how many producers gets eaten
     */
    fun getEaten(producerPopulation: Population, consumerPopulation: Population, consumer: ResourceConsumer, consumeFactor: Double): Population {
        return producerPopulation * consumeFactor * consumerPopulation.populationSize
    }

    fun getResourcesPerInstance(): Resources
}
