package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceConsumer
import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

class Species(
    override val name: String,
    override val features: List<Feature> = emptyList()
) : ResourceProducer, ResourceConsumer,
    TraitBearer {
    constructor(name: String, vararg features: Feature) : this(name, listOf(*features))


    override fun getResourcesPerInstance(): Resources {
        return Resources()
    }

    override fun <T : TraitBearer> T.creator(): (String, Iterable<Feature>) -> T {
        return { name: String, features: Iterable<Feature> -> Species(name, features.toList()) as T }
    }

    override fun canConsume(producer: ResourceProducer) = canHuntFood(producer) || canConsumeFood(producer)

    private fun canConsumeFood(producer: ResourceProducer): Boolean {
        return traits()
            .filterIsInstance<ConsumerTrait>()
            .map { it.influencedResource }
            .any { consumedResource -> producer.traits().filterIsInstance<ProduceResource>().any { it.resourceType == consumedResource } }
    }

    private fun canHuntFood(producer: ResourceProducer): Boolean {
        return traits()
            .filterIsInstance<Predator>()
            .map { it.preyTrait }
            .any { producer.traits().contains(it) }
    }

    override fun consume(consumerPopulation: Population, availableResources: Resources): Population {
        if (0 >= availableResources.getQuantities().sumByDouble { it.amount }) {
            return consumerPopulation * .95
        }
        return consumerPopulation * 1.01
    }

    override fun equals(other: Any?): Boolean {
        return if (null == other || other !is Species) {
            false
        } else {
            this.name == other.name
                    && this.traits() == other.traits()

        }
    }

    override fun hashCode(): Int {
        return name.hashCode() * 13 + traits().sumBy { it.hashCode() * 23 }
    }

}

fun defaultSpecies(name: String = "DefaultSpecies${Math.random()}"): Species {
    return Species(
        name, Feature(
            ConsumerTrait(ResourceType.Water),
            ConsumerTrait(ResourceType.Minerals),
            ConsumerTrait(ResourceType.Energy),
            NeedResource(ResourceType.Water),
            NeedResource(ResourceType.Minerals),
            NeedResource(ResourceType.Energy),
            ProduceResource(ResourceType.EvolutionPoints)
        )
    )
}
