package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

data class Consumption(
    val consumer: Species,
    var needs: Resources,
    var supply: Resources,
    var usableSupply: Resources = Resources(DoubleArray(ResourceType.values().size))
) {
    /**
     * use value objects for resources
     */
    fun times(
        evolutionPointsFactor: Double = 1.0,
        energyFactor: Double = 1.0,
        waterFactor: Double = 1.0,
        mineralsFactor: Double = 1.0
    ): Consumption {
        needs *= ResourceFactor(evolutionPointsFactor, energyFactor, waterFactor, mineralsFactor)
        return this
    }

    fun getPopulation(species: Species = consumer) = supply.get(species)

    fun consume(): Resources {
        //TODO should ensure that supply is not exeeded
        return supply.minus(needs)
    }

    fun isProvided(): Double {
        val canProvide = usableSupply.canProvide(needs)
        return canProvide.values.sumBy { if (it) 1 else 0 } / canProvide.size.toDouble()
    }
}
