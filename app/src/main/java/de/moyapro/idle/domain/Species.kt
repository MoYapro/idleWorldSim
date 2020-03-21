package de.moyapro.idle.domain

import kotlin.math.pow

class Species(
    private val name: String = "DefaultSpecies",
    var individualsInMillons: Double = 1.0
) {
    private val traits: MutableList<Trait> = mutableListOf()

    fun generationAndComsumption(seconds: Int = 1): Resources {
        var result = 1.0
        for (trait in traits) {
            result = trait.influence(result)
        }
        return Resources(evolutionPoints = result * seconds, water = -1, minerals = -1, energy = -1).times(individualsInMillons)
    }

    fun evolve(trait: Trait): Species {
        this.traits.add(trait)
        return this
    }

    fun grow(seconds: Int = 1): Species {
        this.individualsInMillons *= 1.1.pow(seconds)
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
