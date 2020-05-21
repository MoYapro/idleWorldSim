package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.DeathRate
import de.moyapro.idleworldsim.domain.valueObjects.GrowthRate
import de.moyapro.idleworldsim.domain.valueObjects.StarvationRate
import kotlin.math.pow

open class GrowthModifyingTrait : Trait() {
    open fun influenceGrowth(growthRate: GrowthRate): GrowthRate = growthRate
    open fun influenceHunger(starvationRate: StarvationRate): StarvationRate = starvationRate
    open fun influenceDying(deathRate: DeathRate): DeathRate = deathRate
}

object GrowthTrait : GrowthModifyingTrait() {
    override fun influenceGrowth(growthRate: GrowthRate) = growthRate.pow(level.level + 1)
}

object LowDeathRate : GrowthModifyingTrait() {
    override fun influenceDying(deathRate: DeathRate): DeathRate {
        return deathRate * (1 / (1 + (level.level / 24.0).pow(0.99)))
    }
}

private fun Int.pow(exponent: Double) = this.toDouble().pow(exponent)

object HighDeathRate : GrowthModifyingTrait() {
    override fun influenceDying(deathRate: DeathRate) = deathRate * (1 / (1 + (level.level / 12.0).pow(0.99)))

}
