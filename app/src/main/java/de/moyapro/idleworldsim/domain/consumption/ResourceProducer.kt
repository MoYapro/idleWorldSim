package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.domain.valueObjects.Population

interface ResourceProducer : Species {

    fun getEaten(producerPopulation: Population, consumerPopulation: Population, consumer: ResourceConsumer, consumeFactor: Double): Population {
        return Population(0.0)
    }

}
