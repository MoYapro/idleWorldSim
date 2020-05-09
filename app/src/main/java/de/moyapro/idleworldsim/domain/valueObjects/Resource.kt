package de.moyapro.idleworldsim.domain.valueObjects

data class Resource(val resourceType: ResourceType, val amount: Double = 1.0) {
    operator fun times(factor: Double) = Resource(resourceType, amount * factor)
    operator fun times(population: Population) = Resource(resourceType, amount * population.populationSize)
}
