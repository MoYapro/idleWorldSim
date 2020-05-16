package de.moyapro.idleworldsim.domain.valueObjects

import kotlin.math.pow

inline class DeathRate(val rate: Double) {
    fun pow(exponent: Int) = DeathRate(rate.pow(exponent))
    operator fun times(factor: Double) = DeathRate(rate * factor)

}
