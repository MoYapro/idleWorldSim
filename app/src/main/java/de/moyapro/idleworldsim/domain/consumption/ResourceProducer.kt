package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Trait
import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.domain.valueObjects.Population

interface ResourceProducer : Species {

    /**
     * Get all traits or subtraits of this producer
     */
    operator fun get(trait: Trait): List<Trait>
    fun getEaten(producerPopulation: Population, consumerPopulation: Population, consumer: ResourceConsumer, consumeFactor: Double): Population {
        return Population(0.0)
    }

}
