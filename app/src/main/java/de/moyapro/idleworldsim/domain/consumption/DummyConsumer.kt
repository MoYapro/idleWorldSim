package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Trait

class DummyConsumer(override val name: String) : ResourceConsumer {

    private val canConsume: MutableList<String> = mutableListOf()
    override fun canConsume(producer: ResourceProducer): Boolean {
        return canConsume.contains(producer.name)
    }

    override fun get(trait: Trait): List<Trait> {
        return emptyList()
    }

    override fun equals(other: Any?): Boolean {
        return if (null == other || other !is DummyConsumer) {
            false
        } else {
            this.name == other.name
        }
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun canConsume(producerName: String): DummyConsumer {
        canConsume += producerName
        return this
    }
}
