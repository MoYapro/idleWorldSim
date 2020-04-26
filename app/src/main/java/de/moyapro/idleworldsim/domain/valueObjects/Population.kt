package de.moyapro.idleworldsim.domain.valueObjects

inline class Population(val value: Double) {
    operator fun compareTo(other: Population) = value.compareTo(other.value)
    operator fun plus(other: Population) = Population(this.value + other.value)
    operator fun times(growthRate: GrowthRate) = Population(value * growthRate.rate)
    operator fun times(hungerRate: HungerRate)  = Population(value * hungerRate.rate)
    operator fun minus(other: Population) = Population(value - other.value)
    operator fun div(divider: Double) = Population(value / divider)


}

fun Iterable<Population>.sum(): Population {
    var sum = Population(0.0)
    this.forEach { sum += it }
    return sum
}



