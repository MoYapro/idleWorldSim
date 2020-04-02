package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.consumption.Consumption

/**
 * Collection of traits
 */
class Feature(private var traits: List<Trait>) {
    constructor(vararg traits: Trait) : this(listOf(*traits))

    fun influence(consumption: Consumption): Consumption {
        val availableConsumption = traits.applyTo(consumption, SupplyModifyingTrait::influence)
        return traits.applyTo(availableConsumption, ConsumptionModifyingTrait::influence)
    }

}