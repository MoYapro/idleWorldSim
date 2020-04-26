package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.consumption.ResourceTypes
import de.moyapro.idleworldsim.domain.consumption.Resources

abstract class NeedModifyingTrait : Trait() {
    abstract fun influenceNeed(need: Resources): Resources
}

class NeedResource(private val resourceType: ResourceTypes) : NeedModifyingTrait() {
    override fun influenceNeed(need: Resources): Resources {
        need[resourceType] = 1.0
        return need
    }
}

class ProduceResource(private val resourceType: ResourceTypes) : NeedModifyingTrait() {
    override fun influenceNeed(need: Resources): Resources {
        need[resourceType] = -1.0
        return need
    }
}

