package de.moyapro.idleworldsim.domain.valueObjects

class PopulationChange(val changeSize: Double) : Comparable<PopulationChange> {
    constructor(populationSize: Int) : this(populationSize.toDouble())

    override fun compareTo(other: PopulationChange): Int {
        return this.changeSize.compareTo(other.changeSize)
    }

    fun isUnchanged(): Boolean = 0.0 == this.changeSize

}

