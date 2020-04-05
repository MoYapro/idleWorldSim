package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.util.applyTo

/**
 * Collection of traits.
 * Features containing the same traits are equal
 */
open class Feature(private val name: String = "GenericFeature", var traits: Set<Trait> = setOf(), var excludedTraits: Set<Trait> = setOf()) {
    constructor(vararg traits: Trait) : this("GenericFeatureFromTraits", setOf(*traits))

    fun influenceConsumption(consumption: Consumption): Consumption {
        val availableConsumption = traits.applyTo(consumption, SupplyModifyingTrait::influence)
        return traits.applyTo(availableConsumption, ConsumptionModifyingTrait::influence)
    }

    // TODO: Double to ValueObject, e.g. GrowthRate
    fun influenceGrowthRate(growthRate: Double): Double {
        return traits.applyTo(growthRate, GrowthModifyingTrait::influenceGrowth)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (null == other && other !is Feature) {
            return false
        }
        other as Feature
        return traitsAreEqual(other) && nameEqual(other)
    }

    private fun nameEqual(other: Feature) = name == other.name

    private fun traitsAreEqual(other: Feature) = traits.size == other.traits.size && this.traits.minus(other.traits).isEmpty()

    override fun hashCode(): Int {
        return traits.size * 23 + traits.fold(1) { sum, trait -> sum + 373 * trait.hashCode() }
    }

    override fun toString(): String {
        return "Feature[$name - ${traits.joinToString(",") { it.toString() }}]"
    }
}
