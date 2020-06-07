package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

abstract class NeedModifyingTrait(val resourceType: ResourceType, level: Level = Level(1)) : Trait(level) {
    abstract fun influenceNeed(need: Resources): Resources

    override fun equals(other: Any?): Boolean {
        if (null == other || other !is NeedModifyingTrait) return false
        return this.resourceType == other.resourceType
    }

    override fun hashCode(): Int {
        return 5039 * resourceType.ordinal
    }

    override fun toString(): String {
        return "NeedResource`                                                                                                                                                                       [${resourceType}]"
    }
}

class NeedResource(resourceType: ResourceType) : NeedModifyingTrait(resourceType) {
    override fun influenceNeed(need: Resources): Resources {
        need[resourceType] = Resource(resourceType, 1.0)
        return need
    }
}

class ProduceResource(resourceType: ResourceType, level: Level = Level(1)) : NeedModifyingTrait(resourceType, level) {
    override fun influenceNeed(need: Resources): Resources {
        need[resourceType] = Resource(resourceType, -1.0)
        return need
    }

    override fun toString(): String {
        return "NeedResource[$resourceType]"
    }

}

