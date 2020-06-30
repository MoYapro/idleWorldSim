package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.consumption.ResourceConsumer
import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

interface Species : ResourceConsumer, ResourceProducer {

    override fun getResourcesPerInstance(): Resources {
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

    override fun consume(consumerPopulation: Population, availableResources: Resources): Population {
        return Population(3.1415)
    }

}
