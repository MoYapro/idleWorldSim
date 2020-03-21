package de.moyapro.idle.domain

data class Resources(
    var evolutionPoints: Double = 0.0,
    var energy: Int = 1000,
    var water: Int = 1000,
    var minerals: Int = 1000
) {
    fun plus(otherResource: Resources): Resources {
        this.evolutionPoints += otherResource.evolutionPoints
        this.water += otherResource.water
        return this
    }

    fun canProvide(resources: Resources): Boolean {
        // negative value in resources means consumption
        return energy + resources.energy >= 0
                && water + resources.water >= 0
                && minerals + resources.minerals >= 0
    }

    fun times(individualsInMillons: Double = 1.0): Resources {
        return when (individualsInMillons) {
            1.0 -> this
            else -> Resources(
                evolutionPoints * individualsInMillons,
                (energy * individualsInMillons).toInt(),
                (water * individualsInMillons).toInt(),
                (minerals * individualsInMillons).toInt()
            )
        }

    }
}