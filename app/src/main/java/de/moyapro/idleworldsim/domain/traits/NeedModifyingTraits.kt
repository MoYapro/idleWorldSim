package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

abstract class NeedModifyingTrait : Trait() {
    abstract fun influenceNeed(need: Resources): Resources
}

class NeedResource(private val resourceType: ResourceType) : NeedModifyingTrait() {
    override fun influenceNeed(need: Resources): Resources {
        need[resourceType] = Resource(resourceType, 1.0)
        return need
    }
}

class ProduceResource(private val resourceType: ResourceType) : NeedModifyingTrait() {
    override fun influenceNeed(need: Resources): Resources {
        need[resourceType] = Resource(resourceType, -1.0)
        return need
    }

    override fun toString(): String {
        return "NeedResource[$resourceType]"
    }

}

