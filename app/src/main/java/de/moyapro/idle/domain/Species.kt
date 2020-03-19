package de.moyapro.idle.domain

import kotlin.math.pow

class Species {
    private var milionsOfIndividuals: Double = 1.0
    private val traits: MutableList<Trait> = mutableListOf()

    fun generate(seconds: Int = 1): Resources {
        var result = milionsOfIndividuals.toDouble()
        for (trait in traits) {
            result = trait.influence(result)
        }
        return Resources(result * seconds)
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
}
