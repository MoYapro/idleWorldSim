package de.moyapro.idleworldsim.domain.valueObjects

data class PopulationChange(val changeSize: Double) : Comparable<PopulationChange> {
    constructor(populationSize: Int) : this(populationSize.toDouble())

    companion object {
        val NO_CHANGE = PopulationChange(0)
    }

    operator fun plus(other: PopulationChange): PopulationChange = PopulationChange(this.changeSize + other.changeSize)

    override fun compareTo(other: PopulationChange): Int {
        return this.changeSize.compareTo(other.changeSize)
    }

    fun isUnchanged(): Boolean = 0.0 == this.changeSize

}

fun <T> Map<T, PopulationChange>.combineWith(other: Map<T, PopulationChange>): Map<T, PopulationChange> {
    val allkeys: Set<T> = this.keys + other.keys
    return allkeys
        .map { key ->
            val value1 = this[key]
            val value2 = other[key]
            val newValue = when {
                null == value2 && null != value1 -> value1
                null == value1 && null != value2 -> value2
                null != value1 && null != value2 -> value1 + value2
                else -> throw IllegalStateException("WTF")
            }
            Pair(key, newValue)
        }.associate { it }
}