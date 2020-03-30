@file:Suppress("FunctionName")

package de.moyapro.idle.domain

import kotlin.math.pow

sealed class Trait(val level: Int = 1) {
    abstract fun influence(consumption: Consumption): Consumption
}

/**
 * Determin which of the available supply is consumeable/reachable by the current species
 * This may also introcude a factor so that only a fraction of all available resources can be consumed
 */
abstract class SupplyModifyingTrait : Trait()

abstract class GrowthModifyingTrait : Trait() {
    abstract fun influenceGrowth(growthRate: Double): Double
}

class GrowthTrait : GrowthModifyingTrait() {
    override fun influence(consumption: Consumption): Consumption = consumption
    override fun influenceGrowth(growthRate: Double) = growthRate.pow(level + 1)
}

class EnergySaver : Trait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(energyFactor = .9)
    }
}

class WaterSaver : Trait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(waterFactor = .9)
    }
}

class MineralSaver : Trait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(mineralsFactor = .9)
    }
}

class EvolutionBooster : Trait() {
    override fun influence(consumption: Consumption): Consumption {
        return consumption.times(evolutionPointsFactor = 1.15)
    }
}

class Predator(private val prey: Species) : SupplyModifyingTrait() {

    override fun influence(consumption: Consumption): Consumption {
        val huntingEfficiency = 0.01
        val predatorPopulation = consumption.getPopulation()
        val preyPopulationNeeds = consumption.needs.getPopulation(prey)
        if (hasPray(consumption)) {
            consumption.supply = consumption.supply * 0.0
            consumption.needs.setPopulation(prey, preyPopulationNeeds + predatorPopulation * huntingEfficiency)
        }
        return consumption
    }

    private fun hasPray(consumption: Consumption) = consumption.supply.populations.entries.any { it.key == prey }
}

class ResourceFactor(
    val evolutionPointsFactor: Double = 1.0,
    val energyFactor: Double = 1.0,
    val waterFactor: Double = 1.0,
    val mineralsFactor: Double = 1.0
)
