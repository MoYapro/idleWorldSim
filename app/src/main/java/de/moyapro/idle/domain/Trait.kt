@file:Suppress("FunctionName")

package de.moyapro.idle.domain

import kotlin.math.ln
import kotlin.math.pow

sealed class Trait(val level: Int = 1) {
    abstract fun influence(consumption: Consumption)
}

class GrowthTrait() : Trait() {
    override fun influence(consumption: Consumption) = Unit

    fun influence(growthRate: Double) = growthRate.pow(level + 1)
}

class EnergySaver() : Trait() {
    override fun influence(consumption: Consumption) {
        consumption.times(ResourceFactor(energyFactor = .9))
    }
}

class WaterSaver() : Trait() {
    override fun influence(consumption: Consumption) {
        consumption.times(ResourceFactor(waterFactor = .9))
    }
}

class MineralSaver() : Trait() {
    override fun influence(consumption: Consumption) {
        consumption.times(ResourceFactor(mineralsFactor = .9))
    }
}

class EvolutionBooster() : Trait() {
    override fun influence(consumption: Consumption) {
        consumption.times(ResourceFactor(evolutionPointsFactor = 1.15))
    }
}

class Predator(private val prey: Species) : Trait() {
    override fun influence(consumption: Consumption) {
        val huntingEfficiency = 0.01
        val predatorPopulation = consumption.getPopulation()
        val preyPopulationNeeds = consumption.needs.getPopulation(prey)
        consumption.needs.setPopulation(prey, preyPopulationNeeds + predatorPopulation * huntingEfficiency)
    }
}

class ResourceFactor(
    val evolutionPointsFactor: Double = 1.0,
    val energyFactor: Double = 1.0,
    val waterFactor: Double = 1.0,
    val mineralsFactor: Double = 1.0
)
