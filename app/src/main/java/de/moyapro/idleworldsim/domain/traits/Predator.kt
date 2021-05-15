package de.moyapro.idleworldsim.domain.traits

class Predator(val preyTrait: Trait) : Trait() {
    override fun equals(other: Any?): Boolean {
        if (null == other || other !is Predator) return false
        return this.preyTrait == other.preyTrait
    }

    override fun hashCode(): Int {
        return 65537 * preyTrait.hashCode()
    }
}
