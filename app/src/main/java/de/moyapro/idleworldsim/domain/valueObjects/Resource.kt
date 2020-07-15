package de.moyapro.idleworldsim.domain.valueObjects

data class Resource(val resourceType: ResourceType, val amount: Double = 1.0) {
    internal constructor(resourceType: ResourceType, amount: Int) : this(resourceType, amount.toDouble())

    operator fun times(factor: Double) = Resource(resourceType, amount * factor)
    operator fun times(population: Population) = Resource(resourceType, amount * population.populationSize)
    operator fun compareTo(other: Resource): Int {
        if (this.resourceType != other.resourceType) {
            throw IllegalArgumentException("Cannot compare resources of different type. Tried to compare $this with $other")
        }
        return this.amount.compareTo(other.amount)
    }

    operator fun div(quotient: Int) = this.amount / quotient

    override fun toString(): String {
        return "Resource[$resourceType=$amount]"
    }

}

