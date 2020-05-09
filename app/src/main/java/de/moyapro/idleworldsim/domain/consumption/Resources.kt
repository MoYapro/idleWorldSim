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
    constructor(species: Species, population: Population) : this(mutableMapOf(), mutableMapOf(Pair(species, population)))

    @Deprecated("Should use Resource value object instead")
    constructor(resources: DoubleArray) : this(resources.mapIndexed { i, value -> Pair(values()[i], value) }.associate { it }.toMutableMap())
    constructor(resourcesList: List<Resource>) : this(resourcesList.associate { Pair(it.resourceType, it.amount) }.toMutableMap())

    fun setPopulation(species: Species, population: Population = Population(1.0)): Resources {
        this.populations[species] = if (population < SpeciesConstants.MINIMAL_POPULATION) Population(0.0) else population
        return this
    }

    fun updatePopulation(species: Species, growthRate: GrowthRate) =
        setPopulation(species, this[species] * growthRate)


    fun updatePopulation(species: Species, hungerRate: HungerRate) =
        setPopulation(species, this[species] * hungerRate)

    fun updatePopulation(species: Species, deathRate: DeathRate) =
        setPopulation(species, this[species] * deathRate)

    fun setQuantity(resource: Resource): Resources {
        this.quantities[resource.resourceType] = resource.amount
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
    operator fun get(resource: ResourceType) = Resource(resource, quantities.getOrElse(resource) { 0.0 })
    operator fun set(resource: ResourceType, quantity: Resource): Resources {
        if (resource != quantity.resourceType) {
            throw IllegalArgumentException("Not allowed to assign with wrong resourceType. Expected $resource, but was ${quantity.resourceType}")
        }
        return setQuantity(quantity)
    }
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
        subtractQuantities(this.quantities, otherResource.quantities),
        subtractPopulations(this.populations, otherResource.populations)
    )

    private fun subtractPopulations(initialPopulation: MutableMap<Species, Population>, toBeRemoved: MutableMap<Species, Population>): MutableMap<Species, Population> {
        val amountCopy = HashMap(initialPopulation)
        toBeRemoved.entries.forEach { amountCopy[it.key] = (amountCopy[it.key] ?: Population(0.0)) - it.value }
        return amountCopy
    }

    private fun subtractQuantities(initialAmount: MutableMap<ResourceType, Double>, toBeRemoved: MutableMap<ResourceType, Double>): MutableMap<ResourceType, Double> {
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
            it[EvolutionPoints] = it[EvolutionPoints] * factor.evolutionPointsFactor
            it[Energy] = it[Energy] * factor.energyFactor
            it[Water] = it[Water] * factor.waterFactor
            it[Minerals] = it[Minerals] * factor.mineralsFactor
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
            resourceType.name + '=' + quantity.toBigDecimal()
        }.joinToString(", ")})[${this.populations.map { (species, population) -> "$species:${population.populationSize.toBigDecimal()}" }}]"
    }


    operator fun times(population: Population): Resources = this * population.populationSize

}

fun emptyResources() = Resources(mutableMapOf(), mutableMapOf())
