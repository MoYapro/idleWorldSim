package de.moyapro.idle.domain

import kotlin.math.pow

class Species(
    private val name: String = "DefaultSpecies"
) {
    var milionsOfIndividuals: Double = 1.0
    private val traits: MutableList<Trait> = mutableListOf()

    fun generationAndComsumption(seconds: Int = 1): Resources {
        var result = milionsOfIndividuals
        for (trait in traits) {
            result = trait.influence(result)
        }
        return Resources(evolutionPoints = result * seconds, water = -1, minerals = -1, energy = -1)
    }

    fun evolve(trait: Trait): Species {
        this.traits.add(trait)
        return this
    }

    fun grow(seconds: Int = 1): Species {
        this.milionsOfIndividuals *= 1.1.pow(seconds)
        return this
    }

    fun die(seconds: Int = 1): Species {
        this.milionsOfIndividuals *= .95.pow(seconds)
        return this
    }

    fun getStatusText(): String {
        return "$name: ${milionsOfIndividuals}M -> ${generationAndComsumption()}"
    }
}
