package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.GrowthRate
import kotlin.math.pow

open class GrowthModifyingTrait : Trait() {
    open fun influenceGrowth(growthRate: GrowthRate): GrowthRate = growthRate
    open fun influenceDying(deathRate: Double): Double = deathRate
}

object GrowthTrait : GrowthModifyingTrait() {
    override fun influenceGrowth(growthRate: GrowthRate) = growthRate.pow(level + 1)
}

object LowDeathRate : GrowthModifyingTrait() {
    override fun influenceDying(deathRate: Double): Double = deathRate.pow(level * -1 - 1)
}

object HighDeathRate : GrowthModifyingTrait() {
    override fun influenceDying(deathRate: Double): Double = deathRate.pow(level + 1)
}
