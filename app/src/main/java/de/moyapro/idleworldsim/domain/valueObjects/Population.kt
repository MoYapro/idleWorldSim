package de.moyapro.idleworldsim.domain.valueObjects

import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.util.sumUsing

class Population(val populationSize: Double) {
    operator fun compareTo(other: Population) = populationSize.compareTo(other.populationSize)
    operator fun plus(other: Population) = Population(this.populationSize + other.populationSize)
    operator fun times(growthRate: GrowthRate) = Population(populationSize * growthRate.rate)
    operator fun times(starvationRate: StarvationRate) =
        Population(populationSize * starvationRate.rate)

    operator fun times(deathRate: DeathRate) = Population(populationSize * deathRate.rate)
    operator fun minus(other: Population) = Population(populationSize - other.populationSize)
    operator fun div(divider: Double) = Population(populationSize / divider)
    operator fun times(scalar: Double) = Population(populationSize * scalar)

    override fun equals(other: Any?): Boolean {
        return other is Population && populationSize.equals(other.populationSize)
    }

    override fun hashCode(): Int {
        return populationSize.hashCode()
    }

    override fun toString(): String {
        return "Population[$populationSize]"
    }

}

fun Iterable<Population>.sum(): Population {
    var sum = Population(0.0)
    this.forEach { sum += it }
    return sum
}

fun addPopulationMaps(
    population1: Map<Species, Population>,
    population2: Map<Species, Population>
): Map<Species, Population> {
    return (population1.asSequence() + population2.asSequence())
        .distinct()
        .groupBy({ it.key }, { it.value })
        .mapValues { (_, populations) ->
            populations.sumUsing(
                { p1, p2 -> p1 + p2 },
                { Population(0.0) })
                ?: Population(0.0)
        }
}
