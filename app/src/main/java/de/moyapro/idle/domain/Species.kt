package de.moyapro.idle.domain

import kotlin.math.pow

class Species(
    private val name: String = "DefaultSpecies",
    var individualsInMillons: Double = 1.0
) {
    private val traits: MutableList<Trait> = mutableListOf()

    fun generationAndComsumption(seconds: Int = 1): Resources {
        var baseGeneration = baseGeneration()
        traits.forEach { baseGeneration = it.influence(baseGeneration) }
        return baseGeneration.times(individualsInMillons * seconds)
    }

    fun evolve(trait: Trait): Species {
        this.traits.add(trait)
        return this
    }

    fun grow(seconds: Int = 1): Species {
        var growthRate = 1.1
        traits.forEach { if(it is GrowthTrait) growthRate = it.influence(growthRate) }
        this.individualsInMillons *= growthRate.pow(seconds)
        return this
    }

    fun die(seconds: Int = 1): Species {
        this.individualsInMillons *= .95.pow(seconds)
        return this
    }

    fun getStatusText(): String {
        return "$name: ${individualsInMillons}M -> ${generationAndComsumption()}"
    }
}
