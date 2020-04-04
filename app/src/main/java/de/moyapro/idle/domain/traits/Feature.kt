package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.util.applyTo

/**
 * Collection of traits.
 * Features containing the same traits are equal
 */
open class Feature(private var traits: List<Trait>) {
    constructor(vararg traits: Trait) : this(listOf(*traits))

    fun influence(consumption: Consumption): Consumption {
        val availableConsumption = traits.applyTo(consumption, SupplyModifyingTrait::influence)
        return traits.applyTo(availableConsumption, ConsumptionModifyingTrait::influence)
    }

    fun influenceGrowth(growthRate: Double): Double {
        return traits.applyTo(growthRate, GrowthModifyingTrait::influenceGrowth)
    }

    override fun equals(other: Any?): Boolean {
        if (null == other && other !is Feature) {
            return false
        }
        val otherFeature = other as Feature
        return this.traits.minus(otherFeature.traits).isEmpty()
    }


}