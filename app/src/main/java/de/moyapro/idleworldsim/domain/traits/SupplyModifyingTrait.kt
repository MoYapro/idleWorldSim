package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.consumption.Consumption
import de.moyapro.idleworldsim.domain.consumption.ResourceTypes
import de.moyapro.idleworldsim.domain.valueObjects.Population

/**
 * Determine which of the available supply is consumable/reachable by the current species
 * This may also introduce a factor so that only a fraction of all available resources can be consumed
 */
abstract class SupplyModifyingTrait : Trait() {
    abstract fun influence(consumption: Consumption): Consumption
}

class Predator(private val preyTrait: Trait) : SupplyModifyingTrait() {

    override fun influence(consumption: Consumption): Consumption {
        val huntingEfficiency = 0.01
        val predatorPopulation = consumption.getPopulation()
        val prey = findPrey(consumption.supply.populations)
        val totalNumberOfPreyInBiome: Population = prey.map { it.value }.sum()
        val totalIndividualsEaten = totalNumberOfPreyInBiome * (1 - huntingEfficiency * predatorPopulation)
        val eatenPerSpecies = prey.map { Pair(it.key, it.value - (it.value / totalNumberOfPreyInBiome * totalIndividualsEaten)) }

        if (prey.isNotEmpty()) {
            consumption.needs[ResourceTypes.Minerals] = 0.0
            consumption.needs[ResourceTypes.Energy] = 0.0
            eatenPerSpecies.forEach { leftoverPrey ->
                consumption.needs.setPopulation(leftoverPrey.first, leftoverPrey.second)
            }
        }
        return consumption
    }

    private fun findPrey(populations: Map<Species, Population>): Map<Species, Population> {
        return populations.filter { it.key.hasTrait(preyTrait) }
    }

    override fun equals(other: Any?): Boolean {
        if (null == other || other !is Predator) return false
        return this.preyTrait == other.preyTrait
    }

    override fun hashCode(): Int {
        return 65537 * preyTrait.hashCode()
    }
}
