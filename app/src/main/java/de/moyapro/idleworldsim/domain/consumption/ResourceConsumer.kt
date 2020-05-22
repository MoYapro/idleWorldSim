package de.moyapro.idleworldsim.domain.consumption

interface ResourceConsumer {
    fun canConsume(producer: ResourceProducer): Boolean

}
