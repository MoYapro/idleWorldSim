@file:Suppress("FunctionName")

package de.moyapro.idle.domain

import kotlin.math.pow

sealed class Trait(val level: Int = 1) {
    abstract fun influence(resources: Resources, species: Species = Species()): Resources
}

class GrowthTrait() : Trait() {
    override fun influence(resources: Resources, species: Species): Resources {
        return resources
    }

    fun influence(growthRate: Double): Double {
        return growthRate.pow(level + 1)
    }

}

class EnergySaver() : Trait() {
    override fun influence(resources: Resources, species: Species): Resources {
        return resources.times(ResourceFactor(energyFactor = .9))
    }
}

class WaterSaver() : Trait() {
    override fun influence(resources: Resources, species: Species): Resources {
        return resources.times(ResourceFactor(waterFactor = .9))
    }
}

class MineralSaver() : Trait() {
    override fun influence(resources: Resources, species: Species): Resources {
        return resources.times(ResourceFactor(mineralsFactor = .9))
    }
}

class EvolutionBooster() : Trait() {
    override fun influence(resources: Resources, species: Species): Resources {
        return resources.times(ResourceFactor(evolutionPointsFactor = 1.15))
    }
}

class Eater() : Trait() {
    override fun influence(resources: Resources, species: Species): Resources {
        return resources.times(ResourceFactor(0.0, 0.0, 0.0, 0.0))
    }
}

class ResourceFactor(
    val evolutionPointsFactor: Double = 1.0,
    val energyFactor: Double = 1.0,
    val waterFactor: Double = 1.0,
    val mineralsFactor: Double = 1.0
)
