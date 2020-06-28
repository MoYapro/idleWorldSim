package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.consumption.Consumption
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

/**
 * Determine which of the available supply is consumable/reachable by the current species
 * This may also introduce a factor so that only a fraction of all available resources can be consumed
 */
abstract class SupplyModifyingTrait : Trait() {
    abstract fun influence(consumption: Consumption): Consumption
}

class Predator(val preyTrait: Trait) : SupplyModifyingTrait() {

    override fun influence(consumption: Consumption): Consumption {
        val huntingEfficiency = 0.01
        val predatorPopulation = consumption.getPopulation()
        val prey = findPrey(consumption.supply.populations)
        val totalNumberOfPreyInBiome = Population(prey.values.map { it.populationSize }.sum())
        val totalIndividualsEaten = totalNumberOfPreyInBiome * (predatorPopulation.populationSize * (1 - huntingEfficiency))
        val eatenPerSpecies = prey
            .map { (species, preyPopulation) -> Pair(species, preyPopulation - (preyPopulation / totalNumberOfPreyInBiome.populationSize * totalIndividualsEaten.populationSize)) }

        if (prey.isNotEmpty()) {
            consumption.needs[ResourceType.Minerals] = Resource(ResourceType.Minerals, 0.0)
            consumption.needs[ResourceType.Energy] = Resource(ResourceType.Energy, 0.0)
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
