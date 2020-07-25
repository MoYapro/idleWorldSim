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

class NeedResource(resourceType: ResourceType, level: Level = Level(1)) : NeedModifyingTrait(resourceType, level) {
    override fun influenceNeed(need: Resources): Resources {
        return Resources(
            need.quantities
                .map { Pair(it.key, if (it.key == resourceType) 1.0 else it.value) }
                .associate { it }
        )
    }
}

class ProduceResource(val resourceType: ResourceType, level: Level = Level(1)) : Trait(level) {
    override fun toString(): String {
        return "ProduceResource[$resourceType]"
    }

    override fun getConsumptionResources(size: Size?): Resources {
        return Resources(
            Resource(
                this.resourceType,
                this.level.level * (size ?: Size(1)).level.level.toDouble()
            )
        )
    }

    override fun equals(other: Any?): Boolean {
        return when {
            super.equals(other) -> true // same instance
            other is ProduceResource
                    && other.resourceType == this.resourceType
                    && other.level == this.level -> true
            else -> false
        }
    }

    override fun hashCode(): Int = 31 * super.hashCode() + resourceType.hashCode()


}

