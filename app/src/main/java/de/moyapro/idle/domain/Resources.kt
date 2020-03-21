package de.moyapro.idle.domain

data class Resources(
    var evolutionPoints: Double = 0.0,
    var energy: Int = 1000,
    var water: Int = 1000,
    var minerals: Int = 1000
) {
    fun add(otherResource: Resources): Resources {
        this.evolutionPoints += otherResource.evolutionPoints
        this.water += otherResource.water
        return this
    }

    fun hasShortage(generatedResources: Resources): Boolean {
        return energy < generatedResources.energy
                || water < generatedResources.water
                || minerals < generatedResources.minerals
    }
}