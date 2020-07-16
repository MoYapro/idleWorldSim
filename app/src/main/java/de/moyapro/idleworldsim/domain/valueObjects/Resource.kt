package de.moyapro.idleworldsim.domain.valueObjects

data class Resource(val resourceType: ResourceType, val amount: Double = 1.0) : Comparable<Resource> {
    internal constructor(resourceType: ResourceType, amount: Int) : this(resourceType, amount.toDouble())

    operator fun times(factor: Double) = Resource(resourceType, amount * factor)
    operator fun times(population: Population) = Resource(resourceType, amount * population.populationSize)
    override operator fun compareTo(other: Resource): Int {
        if (this.resourceType != other.resourceType) {
            throw IllegalArgumentException("Cannot compare resources of different type. Tried to compare $this with $other")
        }
        return this.amount.compareTo(other.amount)
    }

    operator fun div(quotient: Int) = this.amount / quotient
    operator fun div(other: Resource): Double {
        if (this.resourceType != other.resourceType) {
            throw java.lang.IllegalArgumentException("Dividing by non matching resource type: This is $resourceType, other was ${other.resourceType}")
        }
        return this.amount / other.amount
    }

    override fun toString(): String {
        return "Resource[$resourceType=$amount]"
    }

}

