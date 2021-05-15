package de.moyapro.idleworldsim.domain.valueObjects

inline class DeathRate(val rate: Double) {
    operator fun times(factor: Double) = DeathRate(rate * factor)

}
