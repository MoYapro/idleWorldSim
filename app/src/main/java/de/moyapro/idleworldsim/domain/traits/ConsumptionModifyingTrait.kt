package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.consumption.Consumption
import de.moyapro.idleworldsim.domain.consumption.ResourceType

abstract class ConsumptionModifyingTrait : Trait() {
    abstract fun influence(consumption: Consumption): Consumption
}

class ConsumerTrait(private val influencedResource: ResourceType) : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        consumption.usableSupply[influencedResource] = consumption.supply[influencedResource]
        return consumption
    }

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

object EnergySaver : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(energyFactor = .9)
    }
}

object WaterSaver : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(waterFactor = .9)
    }
}

object MineralSaver : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(mineralsFactor = .9)
    }
}

object EvolutionBooster : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(evolutionPointsFactor = 1.15)
    }
}
