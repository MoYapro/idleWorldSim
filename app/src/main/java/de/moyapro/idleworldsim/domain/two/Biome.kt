package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.consumption.FoodChain
import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.valueObjects.Population

class Biome() {
    private val foodChain = FoodChain()
    private val populations: MutableMap<Species, Population> = mutableMapOf()


    fun process(): Biome {
        return this
    }

    fun settle(species: Species, population: Population = Population(1.0)): Biome {
        foodChain.add(species)
        populations[species] = population
        return this
    }

    operator fun get(species: Species): Population {
        return populations[species] ?: Population(0.0)
    }

    fun settle(producer: ResourceProducer): Biome {
        return this
    }
}
