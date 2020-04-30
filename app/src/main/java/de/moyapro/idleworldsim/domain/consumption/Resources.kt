package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.SpeciesConstants
import de.moyapro.idleworldsim.domain.consumption.ResourceType.*
import de.moyapro.idleworldsim.domain.valueObjects.*


data class Resources private constructor(
    private val quantities: MutableMap<ResourceType, Double> = mutableMapOf(*(ResourceType.values().map { Pair(it, if (it == EvolutionPoints) 0.0 else 1000.0) }).toTypedArray()),
    private val populations: MutableMap<Species, Population> = mutableMapOf()
) {
    @Deprecated("Should use Resource value object instead")
    constructor(quantitiesMap: Map<ResourceType, Double>) : this(quantitiesMap.toMutableMap())
    constructor(resource: ResourceType, quantity: Double = 1.0) : this() {
        setQuantity(resource, quantity)
    }

    fun getPopulation(species: Species) = populations.getOrDefault(species, Population(0.0))
    fun setPopulation(species: Species, population: Population = Population(1.0)): Resources {
        this.populations[species] = if (population < SpeciesConstants.MINIMAL_POPULATION) Population(0.0) else population
        return this
    }

    fun updatePopulation(species: Species, growthRate: GrowthRate): Resources {
        return setPopulation(species, getPopulation(species) * growthRate)
    }

    fun updatePopulation(species: Species, hungerRate: HungerRate): Resources {
        return setPopulation(species, getPopulation(species) * hungerRate)
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

    operator fun get(species: Species) = getPopulation(species)
    operator fun get(resource: ResourceType) = quantities.getOrElse(resource.ordinal) { 0.0 }
    operator fun set(resource: ResourceType, quantity: Double) = setQuantity(resource, quantity)
    operator fun plus(otherResource: Resources) = Resources(
        this.quantities.zip(otherResource.quantities).map { it.first + it.second }.toDoubleArray(),
        (this.populations.asSequence() + otherResource.populations.asSequence())
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, values) -> values.sum() }
            .toMutableMap()
    )

    operator fun minus(otherResource: Resources) = Resources(
        this.quantities.zip(otherResource.quantities).map { it.first - it.second }.toDoubleArray(),
        (this.populations.asSequence() + otherResource.populations.asSequence())
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, values) ->
                if (values.size == 1) values[0] else values[0] - values[1]
            }
            .toMutableMap()
    )

    operator fun times(scalar: Double) = Resources(
        this.quantities.map { it * scalar }.toDoubleArray(),
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

        if (!quantities.contentEquals(other.quantities)) return false
        if (populations != other.populations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = quantities.contentHashCode()
        result = 31 * result + populations.hashCode()
        return result
    }

    fun getSpecies(): Array<Species> = populations.map {
        it.key
    }.toTypedArray()

    override fun toString(): String {
        return "Resources(${this.quantities.mapIndexed { index, quantity ->
            values()[index].displayName + '=' + quantity.toBigDecimal()
        }.joinToString(", ")})"
    }


    operator fun times(population: Population): Resources = this * population

}

fun emptyResources(): Resources = Resources(DoubleArray(values().size) { 0.0 })
fun defaultResources():
