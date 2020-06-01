package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Trait

class DummyProducer(override val name: String) : ResourceProducer {


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

    override fun get(trait: Trait): List<Trait> {
        return emptyList()
    }

}
