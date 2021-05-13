package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceConsumer
import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.PopulationChange
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

open class Species(
    override val name: String,
    override val features: List<Feature> = emptyList()
) : ResourceProducer, ResourceConsumer, TraitBearer {
    companion object {
        private const val MAX_GROWTH = 0.01
    }

    private var resourcesConsumed: Resources = emptyResources()

    constructor(name: String, vararg features: Feature) : this(name, listOf(*features))

    override fun <T : TraitBearer> T.creator(): (String, Iterable<Feature>) -> TraitBearer {
        return { name: String, features: Iterable<Feature> -> Species(name, features.toList()) }
    }

    override fun canConsume(producer: ResourceProducer) = canHuntFood(producer) || canConsumeFood(producer)

    private fun canConsumeFood(producer: ResourceProducer): Boolean {
        return traits()
            .filterIsInstance<ConsumerTrait>()
            .map { it.influencedResource }
            .any { consumedResource ->
                producer.traits().filterIsInstance<ProduceResource>().any { it.resourceType == consumedResource }
            }
    }

    private fun canHuntFood(producer: ResourceProducer): Boolean {
        return traits()
            .filterIsInstance<Predator>()
            .map { it.preyTrait }
            .any { it -> producer.traits().map { it::class.java }.contains(it::class.java) }
    }

    override fun consume(consumerPopulation: Population, availableResources: Resources) {
        this.resourcesConsumed += availableResources
    }

    override fun needs(): List<Resource> {
        val totalNeeds = Resources(traits().filterIsInstance<NeedResource>()
            .groupBy { it.resourceType }
            .map { Resource(it.key, it.value.sumBy { needTrait -> needTrait.level.level }) }
        )
        val currentNeeds = totalNeeds.toList().mapNotNull { resource ->
            val alreadyConsumed = resourcesConsumed[resource.resourceType]
            when {
                alreadyConsumed < resource -> (resource - alreadyConsumed)
                else -> null // this resource is satisfied
            }
        }
        return currentNeeds
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

    override fun toString(): String {
        return "Species[$name, ${features.joinToString(",") { it.name }}]"
    }

    fun grow(speciesPopulation: Population): PopulationChange {
        val numberOfUnfullfilledNeeds = needs()
            .map { neededResourcePerIndivituum -> neededResourcePerIndivituum * speciesPopulation }
            .map { neededResourceTotal ->
                Pair(
                    neededResourceTotal.resourceType,
                    resourcesConsumed[neededResourceTotal.resourceType].amount >= neededResourceTotal.amount
                )
            }
            .sumBy { (_, hasNeedFullfilled) ->
                when (hasNeedFullfilled) {
                    true -> 0
                    false -> 1
                }
            }
        resourcesConsumed = emptyResources() // reset for next turn
        return speciesPopulation * (MAX_GROWTH - (numberOfUnfullfilledNeeds / 100.0))
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
