package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.valueObjects.*
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.EvolutionPoints
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.values
import java.util.*


data class Resources(
    val quantities: Map<ResourceType, Double> = values().associateBy(
        { it },
        { if (it == EvolutionPoints) 0.0 else 1000.0 })
) {
    constructor(resourcesList: List<Resource>) : this(resourcesList.associate { Pair(it.resourceType, it.amount) }
        .toMap())

    constructor(resource: Resource) : this(listOf(resource))
    constructor(vararg resource: Resource) : this(resource.asList())
    constructor(allAmounts: Int) : this(
        values()
            .map { Resource(it, allAmounts) }
    )

    init {
        require(quantities.none { resource -> resource.value < 0 }) { "Resources must not be negative but were: $quantities" }
        require(quantities.none { resource -> resource.value == Double.POSITIVE_INFINITY }) { "Resources must not be infinite" }
    }

    fun canProvide(resources: Resources): Map<ResourceType, Boolean> {
        // negative value in resources means production
        return this.quantities
            .map {
                Pair(it.key, resources.quantities[it.key] ?: 0.0 <= it.value)
            }
            .associate { it }
    }

    operator fun get(resource: ResourceType) = Resource(resource, quantities.getOrElse(resource) { 0.0 })

    operator fun plus(otherResource: Resources): Resources {
        return Resources(calculatePlus(this.quantities, otherResource.quantities))

    }

    private fun calculatePlus(
        quantities1: Map<ResourceType, Double>,
        quantities2: Map<ResourceType, Double>
    ): Map<ResourceType, Double> {
        val newList = LinkedList(quantities1.entries)
        newList.addAll(quantities2.entries)
        return newList.groupBy { it.key }
            .map { it -> Pair(it.key, it.value.sumByDouble { it.value }) }
            .associate { it }
            .toMap()
    }


    operator fun minus(otherResource: Resources): Resources {
        val resources = Resources(subtractQuantities(this.quantities, otherResource.quantities))
        return if (resources.quantities.all { 0 <= it.value })
            resources
        else throw IllegalStateException("Resources cannot be negative")
    }

    private fun subtractQuantities(
        initialAmount: Map<ResourceType, Double>,
        toBeRemoved: Map<ResourceType, Double>
    ): Map<ResourceType, Double> {
        val amountCopy = HashMap(initialAmount)
        toBeRemoved.entries.forEach { amountCopy[it.key] = (amountCopy[it.key] ?: 0.0) - it.value }
        return amountCopy
    }

    operator fun times(population: Population): Resources = this * population.populationSize
    operator fun times(population: PopulationChange): Resources = this * population.changeSize
    operator fun times(scalar: Double) =
        Resources(this.quantities.map { Pair(it.key, it.value * scalar) }.associate { it }.toMap())

    operator fun times(scalar: Int): Resources = this * scalar.toDouble()
    operator fun times(level: Level): Resources = this * level.level

    operator fun times(factor: ResourceFactor): Resources =
        Resources(
            quantities
                .map { Pair(it.key, it.value * factor[it.key]) }
                .associate { it }
        )


    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is Resources -> false
            !resourcesEqual(other) -> false
            else -> true
        }
    }

    private fun resourcesEqual(other: Resources) =
        values()
            .all { this[it] == other[it] }

    override fun hashCode(): Int = quantities.entries.sumBy {
        it.key.hashCode() * it.value.hashCode()
    }

    override fun toString(): String {
        return "Resources(${
            this.quantities.map { (resourceType, quantity) ->
                resourceType.name + '=' + quantity.toBigDecimal()
            }.joinToString(", ")
        })"
    }


    fun getQuantities(): Collection<Resource> =
        quantities.map { (resourceType, amount) -> Resource(resourceType, amount) }

    fun toList(): List<Resource> {
        return quantities.map { (resourceType, value) ->
            Resource(resourceType, value)
        }
    }
}

fun emptyResources() = Resources(mapOf())
