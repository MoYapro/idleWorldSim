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
}

class EnergySaver : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(energyFactor = .9)
    }
}

class WaterSaver : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(waterFactor = .9)
    }
}

class MineralSaver : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(mineralsFactor = .9)
    }
}

class EvolutionBooster : ConsumptionModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(evolutionPointsFactor = 1.15)
    }
}