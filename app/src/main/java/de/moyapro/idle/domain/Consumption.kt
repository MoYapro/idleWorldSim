package de.moyapro.idle.domain

data class Consumption(
    val consumer: Species,
    var needs: Resources,
    val supply: Resources
) {
    operator fun times(factor: ResourceFactor) {
        needs = needs.times(factor)
    }

    fun getPopulation(species: Species = consumer) = supply.getPopulation(species)
}
