package de.moyapro.idleworldsim.domain.valueObjects

inline class Population(val populationSize: Double) {
    operator fun compareTo(other: Population) = populationSize.compareTo(other.populationSize)
    operator fun plus(other: Population) = Population(this.populationSize + other.populationSize)
    operator fun times(growthRate: GrowthRate) = Population(populationSize * growthRate.rate)
    operator fun times(hungerRate: HungerRate) = Population(populationSize * hungerRate.rate)
    operator fun minus(other: Population) = Population(populationSize - other.populationSize)
    operator fun div(divider: Double) = Population(populationSize / divider)
    operator fun times(scalar: Double) = Population(populationSize * scalar)
}

fun Iterable<Population>.sum(): Population {
    var sum = Population(0.0)
    this.forEach { sum += it }
    return sum
}



