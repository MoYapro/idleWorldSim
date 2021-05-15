package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceConsumer
import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.PopulationChange
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*

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
                producer.asTraitBearer().traits().filterIsInstance<ProduceResource>().any { it.resourceType == consumedResource }
            }
    }

    private fun canHuntFood(producer: ResourceProducer): Boolean {
        return traits()
            .filterIsInstance<Predator>()
            .map { it.preyTrait }
            .any { it -> producer.asTraitBearer().traits().map { it::class.java }.contains(it::class.java) }
    }

    override fun consume(consumerPopulation: Population, availableResources: Resources) {
        this.resourcesConsumed += availableResources
    }

    override fun currentNeed(population: Population): List<Resource> {
        val totalNeeds = needsPerPopulation() * population
        return totalNeeds.toList().mapNotNull { resource ->
            val alreadyConsumed = resourcesConsumed[resource.resourceType]
            when {
                alreadyConsumed < resource -> (resource - alreadyConsumed)
                else -> null // this resource is satisfied
            }
        }
    }

    private fun needsPerPopulation(): Resources {
        return Resources(traits().filterIsInstance<NeedResource>()
            .groupBy { it.resourceType }
            .map { Resource(it.key, it.value.sumBy { needTrait -> needTrait.level.level }) }
        )
    }

    override fun asTraitBearer(): TraitBearer {
        return this
    }

    override fun getResourcesPerInstance(): Resources {
        TODO("Not yet implemented")
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
        val totalNeedOfPopulation = needsPerPopulation() * speciesPopulation
        val numberOfUnfullfilledNeeds = calculateNumberOfUnfullfilledNeeds(totalNeedOfPopulation)
        resourcesConsumed = emptyResources() // reset for next turn
        if (numberOfUnfullfilledNeeds > 0) {
            return calculatePopulationStarvation(speciesPopulation, numberOfUnfullfilledNeeds)
        }
        return calculateNewPopulation(totalNeedOfPopulation, speciesPopulation)
    }

    private fun calculatePopulationStarvation(
        speciesPopulation: Population,
        numberOfUnfullfilledNeeds: Int
    ): PopulationChange {
        return speciesPopulation * (MAX_GROWTH - (numberOfUnfullfilledNeeds / 100.0))
    }

    private fun calculateNewPopulation(
        totalNeedOfPopulation: Resources,
        speciesPopulation: Population
    ): PopulationChange {
        val leftoverAfterCurrentPouplationHasEaten = resourcesConsumed - totalNeedOfPopulation
        val resourcesNeededPerNewPopulation = needsPerPopulation()
        val maxPossibleNewPopulation = calculateMaxNewPopulationBaseOnResources(
            leftoverAfterCurrentPouplationHasEaten,
            resourcesNeededPerNewPopulation
        )
        val maxGrowthFromGrothrate = speciesPopulation * MAX_GROWTH
        return minOf(maxPossibleNewPopulation, maxGrowthFromGrothrate)
    }

    private fun calculateMaxNewPopulationBaseOnResources(
        resourcesAvailableForGrowth: Resources,
        resourcesNeededPerNewPopulation: Resources
    ): PopulationChange {
        val changeSize = when {
            resourcesAvailableForGrowth.getQuantities().isEmpty() -> 0.0
            resourcesNeededPerNewPopulation.getQuantities().isEmpty() -> 0.0 // cannot grow without consumption
            else -> resourcesAvailableForGrowth.getQuantities()
                .mapNotNull { (resourceType, amount) ->
                    when (val requiredPerPopulation = resourcesNeededPerNewPopulation[resourceType].amount) {
                        0.0 -> null //ignore not required resource
                        else -> amount / requiredPerPopulation
                    }
                }
                .min() ?: 0.0 // possible growth from most rare resource
        }
        return PopulationChange(changeSize)
    }


    private fun calculateNumberOfUnfullfilledNeeds(totalNeed: Resources): Int {
        val fullfilledPerResource = totalNeed.getQuantities()
            .map { neededResourceTotal ->
                Pair(
                    neededResourceTotal.resourceType,
                    resourcesConsumed[neededResourceTotal.resourceType].amount >= neededResourceTotal.amount
                )
            }

        @Suppress("UnnecessaryVariable") // for clarification and debugging
        val numberOfUnfullfilledNeeds = fullfilledPerResource
            .sumBy { (_, hasNeedFullfilled) ->
                when (hasNeedFullfilled) {
                    true -> 0
                    false -> 1
                }
            }
        return numberOfUnfullfilledNeeds
    }
}


fun defaultSpecies(name: String = "DefaultSpecies${Math.random()}"): Species {
    return Species(
        name, Feature(
            ConsumerTrait(Water),
            ConsumerTrait(Minerals),
            ConsumerTrait(Energy),
            NeedResource(Water),
            NeedResource(Minerals),
            NeedResource(Energy),
            ProduceResource(EvolutionPoints)
        )
    )
}
