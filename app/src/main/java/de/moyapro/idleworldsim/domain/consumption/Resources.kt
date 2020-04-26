package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.SpeciesConstants
import de.moyapro.idleworldsim.domain.consumption.ResourceTypes.*
import de.moyapro.idleworldsim.domain.valueObjects.GrowthRate
import de.moyapro.idleworldsim.domain.valueObjects.HungerRate
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.sum

data class Resources(
    val quantities: DoubleArray = DoubleArray(values().size) { if (it == EvolutionPoints.ordinal) 0.0 else 1000.0 },
    val populations: MutableMap<Species, Population> = mutableMapOf()
) {
    constructor(quantitiesMap: Map<ResourceTypes, Double>) : this(mapToDoubleArray(quantitiesMap))

    companion object {
        private fun mapToDoubleArray(quantitiesMap: Map<ResourceTypes, Double>): DoubleArray {
            return DoubleArray(values().size) { quantitiesMap[values()[it]] ?: 0.0 }
        }
    }

    operator fun get(species: Species) = getPopulation(species)

    fun getPopulation(species: Species) = populations.getOrDefault(species, Population(0.0))

    fun setPopulation(species: Species, population: Population = Population(1.0)): Resources {
        this.populations[species] = if (population < SpeciesConstants.MINIMAL_POPULATION) Population(0.0) else population
        return this
    }

    operator fun get(resource: ResourceTypes) = quantities.getOrElse(resource.ordinal) { 0.0 }
    operator fun set(resource: ResourceTypes, quantity: Double) = setQuantity(resource, quantity)


    fun setQuantity(resource: ResourceTypes, quantity: Double = 1.0): Resources {
        if (resource.ordinal in this.quantities.indices)
            this.quantities[resource.ordinal] = quantity
        return this
    }

    override fun toString(): String {
        return "Resources(${this.quantities.mapIndexed { index, quantity ->
            values()[index].displayName + '=' + quantity.toBigDecimal()
        }.joinToString(", ")})"
    }

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

    fun canProvide(resources: Resources): BooleanArray {
        // negative value in resources means production
        return this.quantities
            .withIndex()
            .map {
                resources.quantities[it.index] <= it.value
            }
            .toBooleanArray()
    }

    fun updatePopulation(species: Species, growthRate: GrowthRate): Resources {
        return setPopulation(species, getPopulation(species) * growthRate)
    }
    fun updatePopulation(species: Species, hungerRate: HungerRate): Resources {
        return setPopulation(species, getPopulation(species) * hungerRate)
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

    operator fun times(population: Population): Resources = this. * population

    constructor(resource: Resource, quantity: Double = 1.0) : this() {
        setQuantity(resource, quantity)
    }
}

fun emptyResources(): Resources = Resources(DoubleArray(values().size) { 0.0 })
