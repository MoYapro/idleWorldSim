package de.moyapro.idleworldsim.domain.valueObjects

import kotlin.math.pow

inline class GrowthRate(val rate: Double) {
    fun pow(exponent: Int) = GrowthRate(rate.pow(exponent))
    operator fun times(other: GrowthRate) = this.rate * other.rate
}
