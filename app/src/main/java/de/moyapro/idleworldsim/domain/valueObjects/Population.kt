package de.moyapro.idleworldsim.domain.valueObjects

import de.moyapro.idleworldsim.domain.TraitBearer
import de.moyapro.idleworldsim.util.sumUsing

class Population(val populationSize: Double) : Comparable<Population> {
    constructor(populationSize: Int) : this(populationSize.toDouble())

    init {
        require(0 <= populationSize) { "Population size must not be negative. Perhaps you would like to use PopulationChange or StarvationRate" }
    }

    override operator fun compareTo(other: Population) = populationSize.compareTo(other.populationSize)
    operator fun plus(other: Population) = Population(this.populationSize + other.populationSize)
    operator fun plus(other: PopulationChange) = Population(this.populationSize + other.changeSize)
    operator fun times(growthRate: GrowthRate) = Population(populationSize * growthRate.rate)
    operator fun times(starvationRate: StarvationRate) = PopulationChange(populationSize * starvationRate.rate)
    operator fun times(scalar: Double) = PopulationChange(populationSize * scalar)
    operator fun times(scalar: Int) = PopulationChange(this.populationSize * scalar)
    operator fun times(deathRate: DeathRate): PopulationChange {
        return when {
            deathRate.rate >= 1 -> PopulationChange(populationSize)
            else -> PopulationChange(populationSize * deathRate.rate)
        }
    }

    operator fun minus(other: PopulationChange) = Population(populationSize - other.changeSize)
    operator fun div(divider: Double) = Population(populationSize / divider)

    override fun equals(other: Any?): Boolean {
        return other is Population && populationSize.equals(other.populationSize)
    }

    override fun hashCode(): Int {
        return populationSize.hashCode()
    }

    override fun toString(): String {
        return "Population[$populationSize]"
    }

    fun isNotEmpty(): Boolean = 0 < this.populationSize

    fun isEmpty(): Boolean = 0 >= this.populationSize
    fun asPopulationChange() = PopulationChange(this.populationSize)


}

fun addPopulationMaps(
    population1: Map<TraitBearer, Population>,
    population2: Map<TraitBearer, Population>
): Map<TraitBearer, Population> {
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
