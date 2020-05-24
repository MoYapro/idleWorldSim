package de.moyapro.idleworldsim.domain.consumption

interface ResourceConsumer {
    val name: String

    fun canConsume(producer: ResourceProducer): Boolean

}
