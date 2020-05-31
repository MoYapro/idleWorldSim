package de.moyapro.idleworldsim.domain.consumption

class DummyPorC(override val name: String) : PorC {

    private val canConsume: MutableList<String> = mutableListOf()
    override fun canConsume(producer: ResourceProducer): Boolean {
        return canConsume.contains(producer.name)
    }

    override fun consumePowerIndex(producer: ResourceProducer) {
        TODO("Not yet implemented")
    }


    override fun equals(other: Any?): Boolean {
        return if (null == other || other !is DummyPorC) {
            false
        } else {
            this.name == other.name
        }
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun canConsume(producerName: String): PorC {
        canConsume += producerName
        return this
    }
}
