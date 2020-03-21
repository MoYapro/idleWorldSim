@file:Suppress("FunctionName")

package de.moyapro.idle.domain

sealed class Trait(private val level: Int = 1) {
    abstract fun influence(resources: Resources): Resources
}

class GrowthTrait() : Trait() {
    override fun influence(resources: Resources): Resources {
        return resources
    }
}

class EnergySaver() : Trait() {
    override fun influence(resources: Resources): Resources {
        return resources.times(ResourceFactor(energyFactor = .9))
    }
}

class WaterSaver() : Trait() {
    override fun influence(resources: Resources): Resources {
        return resources.times(ResourceFactor(waterFactor = .9))
    }
}

class EvolutionBooster() : Trait() {
    override fun influence(resources: Resources): Resources {
        return resources.times(ResourceFactor(evolutionPointsFactor = 1.15))
    }
}


class ResourceFactor(
    val evolutionPointsFactor: Double = 1.0,
    val energyFactor: Double = 1.0,
    val waterFactor: Double = 1.0,
    val mineralsFactor: Double = 1.0
)