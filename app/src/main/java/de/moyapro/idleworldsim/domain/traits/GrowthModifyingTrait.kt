package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.DeathRate
import de.moyapro.idleworldsim.domain.valueObjects.GrowthRate
import de.moyapro.idleworldsim.domain.valueObjects.StarvationRate

open class GrowthModifyingTrait : Trait() {
    open fun influenceGrowth(growthRate: GrowthRate): GrowthRate = growthRate
    open fun influenceHunger(starvationRate: StarvationRate): StarvationRate = starvationRate
    open fun influenceDying(deathRate: DeathRate): DeathRate = deathRate
}

object GrowthTrait : GrowthModifyingTrait() {
    override fun influenceGrowth(growthRate: GrowthRate) = growthRate.pow(level.level + 1)
}

object LowDeathRate : GrowthModifyingTrait() {
    override fun influenceDying(deathRate: DeathRate): DeathRate = deathRate.pow(level.level * -1 - 1)
}

object HighDeathRate : GrowthModifyingTrait() {
    override fun influenceDying(deathRate: DeathRate): DeathRate = deathRate.pow(level.level + 1)
}
