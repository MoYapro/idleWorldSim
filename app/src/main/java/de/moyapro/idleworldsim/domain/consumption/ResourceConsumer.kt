package de.moyapro.idleworldsim.domain.consumption

interface ResourceConsumer {
    val name: String

    fun canConsume(producer: ResourceProducer): Boolean

    /**
     * Calculate an index indicating how got the consume can find/hunt/eat the producer
     */
     fun consumePowerIndex(producer: ResourceProducer): Double

}
