package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.TraitBearer
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource


interface ResourceConsumer {
    val minimumFactor: Double
        get() = 0.01

    @Deprecated("FoodChain should not depend on TraitBearer")
    fun asTraitBearer(): TraitBearer

    fun canConsume(producer: ResourceProducer): Boolean

    fun calculatePreferenceIndex(producer: ResourceProducer, consumePowerFactor: Double): Double {
        return when (canConsume(producer)) {
            false -> 0.0
            true -> consumePowerFactor * producer.getResourcesPerInstance().getQuantities().sumByDouble { it.amount }
        }

        /*
        + resources gained relative to others
        + number of positive traits gotten from consuming that producer
        - number of negative traits gotten from consuming that producer
         */
    }


    /**
     * consume available resources and may update its populations
     */
    fun consume(consumerPopulation: Population, availableResources: Resources)
    fun currentNeed(population: Population): List<Resource>
}
