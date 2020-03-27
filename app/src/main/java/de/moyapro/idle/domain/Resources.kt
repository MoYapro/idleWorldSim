package de.moyapro.idle.domain

data class Resources(
    val evolutionPoints: Double = 0.0,
    val energy: Double = 1000.0,
    val water: Double = 1000.0,
    val minerals: Double = 1000.0,
    val populations: MutableMap<Species, Double> = mutableMapOf()
) {
    fun getPopulation(species: Species) = populations[species] ?: 0.0

    fun setPopulation(species: Species, population: Double) : Resources {
        this.populations[species] = if (population < 1E-6) 0.0 else population
        return this
    }

    override fun toString() : String {
        return "Resources(evp=$evolutionPoints, nrg=$energy, h20=$water, ore=$minerals)"
    }

    operator fun plus(otherResource: Resources) = Resources(
        this.evolutionPoints + otherResource.evolutionPoints,
        this.energy + otherResource.energy,
        this.water + otherResource.water,
        this.minerals + otherResource.minerals,
        (this.populations.asSequence() + otherResource.populations.asSequence())
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, values) -> values.sum() }
            .toMutableMap()
    )

    operator fun minus(otherResource: Resources) = Resources(
        this.evolutionPoints - otherResource.evolutionPoints,
        this.energy - otherResource.energy,
        this.water - otherResource.water,
        this.minerals - otherResource.minerals,
        (this.populations.asSequence() + otherResource.populations.asSequence())
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, values) ->
                if (values.size == 1) values[0] else values[0] - values[1]
            }
            .toMutableMap()
    )

    operator fun times(scalar: Double) = Resources(
        this.evolutionPoints * scalar,
        this.energy * scalar,
        this.water * scalar,
        this.minerals * scalar,
        this.populations
    )

    operator fun times(factor: ResourceFactor): Resources {
        return Resources(
            this.evolutionPoints * factor.evolutionPointsFactor,
            this.energy * factor.energyFactor,
            this.water * factor.waterFactor,
            this.minerals * factor.mineralsFactor,
            this.populations
        )
    }

    fun canProvide(resources: Resources): Boolean {
        // negative value in resources means production
        return energy >= resources.energy
                && water >= resources.water
                && minerals >= resources.minerals
    }
}
