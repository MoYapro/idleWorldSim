package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*

class ResourceFactor(
    private val evolutionPointsFactor: Double = 1.0,
    private val energyFactor: Double = 1.0,
    private val waterFactor: Double = 1.0,
    private val mineralsFactor: Double = 1.0
) {
    operator fun get(resourceType: ResourceType): Double {
        return when(resourceType) {
            Water -> waterFactor
            EvolutionPoints -> evolutionPointsFactor
            Energy -> energyFactor
            Minerals -> mineralsFactor
            else -> throw IllegalArgumentException("unknown resourcetype: $resourceType")
        }
    }
}
