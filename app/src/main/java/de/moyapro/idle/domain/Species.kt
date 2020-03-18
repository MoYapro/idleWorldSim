package de.moyapro.idle.domain

import kotlin.math.pow

class Species {
    private var milionsOfIndividuals: Double = 1.0
    private val traits: MutableList<Trait> = ArrayList()

    fun generate(seconds: Int = 1): Double {
        var result = milionsOfIndividuals.toDouble()
        for (trait in traits) {
            result = trait.influence(result)
        }
        return result * seconds
    }

    fun evolve(trait: Trait): Species {
        this.traits.add(trait)
        return this
    }

    fun grow(seconds: Int = 1): Species {
        this.milionsOfIndividuals *= 1.1.pow(seconds)
        return this
    }
}
