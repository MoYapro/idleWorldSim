package de.moyapro.idleworldsim.domain.valueObjects

class Population(val populationSize: Double) : Comparable<Population> {
    constructor(populationSize: Int) : this(populationSize.toDouble())

    init {
        require(0 <= populationSize) { "Population size must not be negative but was $populationSize. Perhaps you would like to use PopulationChange or StarvationRate" }
    }

    override operator fun compareTo(other: Population) = populationSize.compareTo(other.populationSize)

    @Deprecated("This should be an change")
    operator fun plus(other: Population) = Population(this.populationSize + other.populationSize)
    operator fun plus(other: PopulationChange): Population {
        return when (other.changeSize) {
            Double.NEGATIVE_INFINITY -> Population(0)
            Double.POSITIVE_INFINITY -> throw IllegalStateException("Cannot add inifity population")
            else -> Population(this.populationSize + other.changeSize)
        }
    }

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

}

fun <T> Map<T, Population>.applyChanges(changes: Map<T, PopulationChange>): Map<T, Population> {
    val newPopulationValues = changes.map { (species, change) ->
        val newPopulation: Population = (this[species] ?: Population(0)) + change
        species to newPopulation
    }.associate { it }
        .toMutableMap()

    this.forEach { (species, unchangedValue) ->
        val speciesIsUnchanged = null == newPopulationValues[species]
        if (speciesIsUnchanged) {
            newPopulationValues[species] = unchangedValue
        }
    }
    return newPopulationValues.filter { it.value.isNotEmpty() }
}
