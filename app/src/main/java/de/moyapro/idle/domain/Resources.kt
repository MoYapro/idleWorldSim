package de.moyapro.idle.domain

data class Resources(
    val evolutionPoints: Double = 0.0,
    val energy: Int = 1000,
    val water: Int = 1000,
    val minerals: Int = 1000
) {
    fun plus(otherResource: Resources): Resources {
        return Resources(
            this.evolutionPoints + otherResource.evolutionPoints,
            this.energy + otherResource.energy,
            this.water + otherResource.water,
            this.minerals + otherResource.minerals
        )
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

    fun times(factor: ResourceFactor): Resources {
        return Resources(
            this.evolutionPoints * factor.evolutionPointsFactor,
            (this.energy * factor.energyFactor).toInt(),
            (this.water * factor.waterFactor).toInt(),
            (this.minerals * factor.mineralsFactor).toInt()
        )
    }

}

fun baseGeneration() = Resources(1.0, -1000, -1000, -1000)