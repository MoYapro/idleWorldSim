package de.moyapro.idle.domain.traits

import kotlin.math.pow

abstract class GrowthModifyingTrait : Trait() {
    abstract fun influenceGrowth(growthRate: Double): Double
}

object GrowthTrait : GrowthModifyingTrait() {
    override fun influenceGrowth(growthRate: Double) = growthRate.pow(level + 1)
}
