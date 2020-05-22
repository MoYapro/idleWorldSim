package de.moyapro.idleworldsim.domain.consumption

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

}
