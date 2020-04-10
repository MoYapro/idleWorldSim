package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource

abstract class ConsumptionModifyingTrait : Trait() {
    abstract fun influence(consumption: Consumption): Consumption
}

class ConsumerTrait(private val influencedResource: Resource) : ConsumptionModifyingTrait() {
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

object OxygenProducer : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        consumption.needs[Resource.Oxygen] = -1.0
        return consumption
    }


}
