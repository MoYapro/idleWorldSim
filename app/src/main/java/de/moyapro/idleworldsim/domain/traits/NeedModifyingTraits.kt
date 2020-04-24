package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.consumption.Resource
import de.moyapro.idleworldsim.domain.consumption.Resources

abstract class NeedModifyingTrait : Trait() {
    abstract fun influenceNeed(need: Resources): Resources
}

class NeedResource(private val resourceType: Resource) : NeedModifyingTrait() {
    override fun influenceNeed(need: Resources): Resources {
        need[resourceType] = 1.0
        return need
    }
}

class ProduceResource(private val resourceType: Resource) : NeedModifyingTrait() {
    override fun influenceNeed(need: Resources): Resources {
        need[resourceType] = -1.0
        return need
    }
}

