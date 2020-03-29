package de.moyapro.idle.domain

import de.moyapro.idle.domain.Resource.*

data class Resources(
    val quantities: DoubleArray = DoubleArray(values().size) { if (it == EvolutionPoints.ordinal) 0.0 else 1000.0 },
    val populations: MutableMap<Species, Double> = mutableMapOf()
) {
    operator fun get (species: Species) = getPopulation(species)
    operator fun set (species: Species, population: Double) = setPopulation(species, population)

    fun getPopulation(species: Species) = populations.getOrDefault(species,  0.0)

    fun setPopulation(species: Species, population: Double = 1.0) : Resources {
        this.populations[species] = if (population < 1E-6) 0.0 else population
        return this
    }

    operator fun get (resource: Resource) = getQuantity(resource)
    operator fun set (resource: Resource, quantity: Double) = setQuantity(resource, quantity)

    fun getQuantity(resource: Resource) = quantities.getOrElse(resource.ordinal) { 0.0 }

    fun setQuantity(resource: Resource, quantity: Double = 1.0) : Resources {
        if (resource.ordinal in this.quantities.indices)
            this.quantities[resource.ordinal] = quantity
        return this
    }

    override fun toString() : String {
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
        this.quantities.map { it*scalar }.toDoubleArray(),
        this.populations
    )

    operator fun times(factor: ResourceFactor) : Resources {
        return this.copy().let {
            it[EvolutionPoints] *= factor.evolutionPointsFactor
            it[Energy] *= factor.energyFactor
            it[Water] *= factor.waterFactor
            it[Minerals] *= factor.mineralsFactor
            it
        }
    }

    fun canProvide(resources: Resources): Boolean {
        // negative value in resources means production
        return this.quantities.withIndex().all {
            resources.quantities[it.index] <= it.value
        } && this.populations.all {
            (resources.populations[it.key] ?: 0.0) <= it.value
        }
    }

    fun updatePopulation(species: Species, growthRate: Double): Resources {
        return setPopulation(species, getPopulation(species) * growthRate)
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
}
