package de.moyapro.idle.domain

data class Consumption(
    val consumer: Species,
    var needs: Resources,
    var supply: Resources,
    var usableSupply: Resources = Resources(DoubleArray(Resource.values().size))
) {

    operator fun times(factor: ResourceFactor) {
        needs = needs.times(factor)
    }

    /**
     * use value objects for resources
     */
    fun times(
        evolutionPointsFactor: Double = 1.0,
        energyFactor: Double = 1.0,
        waterFactor: Double = 1.0,
        mineralsFactor: Double = 1.0
    ): Consumption {
        needs = needs.times(ResourceFactor(evolutionPointsFactor, energyFactor, waterFactor, mineralsFactor))
        return this
    }

    fun getPopulation(species: Species = consumer) = supply.getPopulation(species)

    fun consume(): Resources {
        //TODO should ensure that supply is not exeeded
        return supply.minus(needs)
    }

    fun isProvided(): Boolean {
        return usableSupply.canProvide(needs)
    }
}
