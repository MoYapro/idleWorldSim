package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.Species
import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource

/**
 * Determine which of the available supply is consumable/reachable by the current species
 * This may also introduce a factor so that only a fraction of all available resources can be consumed
 */
abstract class SupplyModifyingTrait : Trait() {
    abstract fun influence(consumption: Consumption): Consumption
}

class Predator(private val prey: Species) : SupplyModifyingTrait() {

    override fun influence(consumption: Consumption): Consumption {
        val huntingEfficiency = 0.01
        val predatorPopulation = consumption.getPopulation()
        val preyPopulationNeeds = consumption.needs.getPopulation(prey)
        if (hasPrey(consumption)) {
            consumption.needs[Resource.Minerals] = 0.0
            consumption.needs[Resource.Energy] = 0.0
            consumption.needs.setPopulation(prey, preyPopulationNeeds + predatorPopulation * huntingEfficiency)
        }
        return consumption
    }

    private fun hasPrey(consumption: Consumption) = consumption.supply.populations.entries.any { it.key == prey }

    override fun equals(other: Any?): Boolean {
        if (null == other || other !is Predator) return false
        return this.prey == other.prey
    }
}
