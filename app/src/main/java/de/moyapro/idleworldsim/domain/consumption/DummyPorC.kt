package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.Trait

open class DummyPorC(override val name: String, override val features: List<Feature> = emptyList()) : PorC {
    override val creator = ::DummyPorC
    private val canConsume: MutableList<String> = mutableListOf()
    override fun canConsume(producer: ResourceProducer): Boolean {
        return canConsume.contains(producer.name)
    }

    override fun consumePowerFactor(producer: ResourceProducer): Double {
        return 0.4
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

    override fun get(trait: Trait): List<Trait> {
        return emptyList()
    }
}
