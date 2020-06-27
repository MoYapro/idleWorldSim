package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Feature

class DummyProducer(override val name: String, override val features: List<Feature> = emptyList()) :
    ResourceProducer {

    override val creator = ::DummyProducer
    override fun equals(other: Any?): Boolean {
        return if (null == other || other !is DummyProducer) {
            false
        } else {
            this.name == other.name
        }
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
