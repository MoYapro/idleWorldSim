package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.SpeciesConstants
import de.moyapro.idleworldsim.domain.valueObjects.*
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
import java.util.*


data class Resources(
    val quantities: MutableMap<ResourceType, Double> = mutableMapOf(*(values().map { Pair(it, if (it == EvolutionPoints) 0.0 else 1000.0) }).toTypedArray()),
    val populations: MutableMap<Species, Population> = mutableMapOf()
) {
    @Deprecated("Should use Resource value object instead")
    constructor(quantitiesMap: Map<ResourceType, Double>) : this(quantitiesMap.toMutableMap())
    constructor(resource: ResourceType, quantity: Double = 1.0) : this(quantities = mutableMapOf(Pair(resource, quantity)))

    constructor(doubleArrayOf: DoubleArray) : this(doubleArrayOf.withIndex().map { Pair(ResourceType.values()[it.index], it.value) }.associate { it }.toMutableMap())
    constructor(resourcesList: List<Resource>) : this(resourcesList.associate { Pair(it.resourceType, it.amount) }.toMutableMap())

    fun setPopulation(species: Species, population: Population = Population(1.0)): Resources {
        this.populations[species] = if (population < SpeciesConstants.MINIMAL_POPULATION) Population(0.0) else population
        return this
    }

    fun updatePopulation(species: Species, growthRate: GrowthRate): Resources {
        return setPopulation(species, this.get(species) * growthRate)
    }

    fun updatePopulation(species: Species, hungerRate: HungerRate): Resources {
        return setPopulation(species, this.get(species) * hungerRate)
    }

    fun setQuantity(resource: ResourceType, quantity: Double = 1.0): Resources {
        this.quantities[resource] = quantity
        return this
    }

    fun canProvide(resources: Resources): Map<ResourceType, Boolean> {
        // negative value in resources means production
        return this.quantities
            .map {
                Pair(it.key, resources.quantities[it.key] ?: 0.0 <= it.value)
            }
            .associate { it }
    }

    operator fun get(species: Species) = populations.getOrDefault(species, Population(0.0))
    operator fun get(resource: ResourceType) = quantities.getOrElse(resource) { 0.0 }
    operator fun set(resource: ResourceType, quantity: Double) = setQuantity(resource, quantity)
    operator fun plus(otherResource: Resources): Resources {
        return Resources(
            calculatePlus(this.quantities, otherResource.quantities),
            (this.populations.asSequence() + otherResource.populations.asSequence())
                .groupBy({ it.key }, { it.value })
                .mapValues { (_, values) -> values.sum() }
                .toMutableMap()
        )

    }

    private fun calculatePlus(quantities1: MutableMap<ResourceType, Double>, quantities2: MutableMap<ResourceType, Double>): MutableMap<ResourceType, Double> {
        val newList = LinkedList(quantities1.entries)
        newList.addAll(quantities2.entries)
        return newList.groupBy { it.key }
            .map { it -> Pair(it.key, it.value.sumByDouble { it.value }) }
            .associate { it }
            .toMutableMap()


    }


    operator fun minus(otherResource: Resources) = Resources(
        subtract(this.quantities, otherResource.quantities),
        this.populations //- otherResource.populations
    )

    private fun subtract(initialAmount: MutableMap<ResourceType, Double>, toBeRemoved: MutableMap<ResourceType, Double>): MutableMap<ResourceType, Double> {
        val amountCopy = HashMap(initialAmount)
        toBeRemoved.entries.forEach { amountCopy[it.key] = (amountCopy[it.key] ?: 0.0) - it.value }
        return amountCopy
    }

    operator fun times(scalar: Double) = Resources(
        this.quantities.map { Pair(it.key, it.value * scalar) }.associate { it }.toMutableMap(),
        this.populations
    )

    operator fun times(factor: ResourceFactor): Resources {
        return this.copy().let {
            it[EvolutionPoints] *= factor.evolutionPointsFactor
            it[Energy] *= factor.energyFactor
            it[Water] *= factor.waterFactor
            it[Minerals] *= factor.mineralsFactor
            it
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resources

        if (quantities != other.quantities) return false
        if (populations != other.populations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = quantities.entries.sumBy { it.key.hashCode() * it.value.hashCode() }
        result = 31 * result + populations.hashCode()
        return result
    }

    fun getSpecies(): Array<Species> = populations.map {
        it.key
    }.toTypedArray()

    override fun toString(): String {
        return "Resources(${this.quantities.map { (resourceType, quantity) ->
            resourceType.displayName + '=' + quantity.toBigDecimal()
        }.joinToString(", ")})"
    }


    operator fun times(population: Population): Resources = this * population.populationSize

}

fun emptyResources() = Resources(mutableMapOf(), mutableMapOf())
