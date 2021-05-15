package de.moyapro.idleworldsim.domain.traits


import de.moyapro.idleworldsim.domain.valueObjects.ResourceType


class ConsumerTrait(val influencedResource: ResourceType) : Trait() {

    override fun equals(other: Any?): Boolean {
        if (null == other || other !is ConsumerTrait) return false
        return this.influencedResource == other.influencedResource
    }

    override fun hashCode(): Int {
        return 5039 * influencedResource.ordinal
    }

    override fun toString(): String {
        return "ConsumerTrait[${influencedResource}]"
    }

}

object EnergySaver : Trait()
object MineralSaver : Trait()
