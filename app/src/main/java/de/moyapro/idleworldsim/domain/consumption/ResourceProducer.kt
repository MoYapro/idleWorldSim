package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.TraitBearer
import de.moyapro.idleworldsim.domain.valueObjects.Population

interface ResourceProducer  : TraitBearer {

    fun getEaten(producerPopulation: Population, consumerPopulation: Population, consumer: ResourceConsumer, consumeFactor: Double): Population {
        return Population(0.0)
    }

    fun getResourcesPerInstance(): Resources
}
