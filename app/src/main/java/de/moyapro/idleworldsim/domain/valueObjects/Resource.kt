package de.moyapro.idleworldsim.domain.valueObjects

data class Resource(val resourceType: ResourceType, val amount: Double = 1.0) {
    internal constructor(resourceType: ResourceType, amount: Int) : this(resourceType, amount.toDouble())

    operator fun times(factor: Double) = Resource(resourceType, amount * factor)
    operator fun times(population: Population) = Resource(resourceType, amount * population.populationSize)
    operator fun compareTo(other: Resource) = this.amount.compareTo(other.amount)
}
