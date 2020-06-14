package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

interface Species {
    val features: List<Feature>
    val name: String
    val creator: (name: String, features: List<Feature>) -> Species

    /**
     * Species evolves new feature(s) and a new species is created
     */
    fun evolveTo(vararg evolvedFeature: Feature, name: String? = null): Species {
        val newFeatures = mutableListOf(*evolvedFeature)
        newFeatures.addAll(features)
        val newName = name ?: "${this.name}+"
        return creator(newName, newFeatures)
    }

    fun getResourcesPerIndividuum(): Resources {
        return Resources(
            listOf(
                Resource(ResourceType.Minerals, 1.0),
                Resource(ResourceType.Water, 1.0),
                Resource(ResourceType.Energy, 1.0),
                Resource(ResourceType.Oxygen, 1.0),
                Resource(ResourceType.EvolutionPoints, 1.0)
            )
        )

    }

    fun grow(
        population: Population,
        availableResources: Resources
    ): Population {
        TODO("Not yet implemented")
    }

}
