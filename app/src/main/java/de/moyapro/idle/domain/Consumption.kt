package de.moyapro.idle.domain

data class Consumption(
    val consumer: Species,
    var needs: Resources,
    val supply: Resources
) {
    operator fun times(factor: ResourceFactor) {
        needs = needs.times(factor)
    }

    fun times(
        evolutionPointsFactor: Double = 1.0,
        energyFactor: Double = 1.0,
        waterFactor: Double = 1.0,
        mineralsFactor: Double = 1.0
    ) {
        needs = needs.times(ResourceFactor(evolutionPointsFactor, energyFactor, waterFactor, mineralsFactor))
    }

    fun getPopulation(species: Species = consumer) = supply.getPopulation(species)

    fun consume(): Resources {
        return supply.minus(needs)
    }

    fun isProvided(): Boolean {
        return supply.canProvide(needs)
    }
}
