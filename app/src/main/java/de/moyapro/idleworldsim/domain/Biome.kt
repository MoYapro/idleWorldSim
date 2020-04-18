package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.util.toShortDecimalStr
import java.util.*

data class Biome(
    val name: String = "DefaultBiome",
    var resources: Resources = Resources(),
    private val speciesList: MutableCollection<Species> = mutableListOf()
) {
    class BiomeProcessObservable : Observable() {
        fun notifyChange() {
            setChanged()
            notifyObservers()
        }
    }

    val onBiomeProcess = BiomeProcessObservable()

    fun process(): Biome {
        this.resources = speciesList
            .shuffled()
            .fold(resources)
            { leftOvers, species -> species.process(leftOvers) }
        onBiomeProcess.notifyChange()
        return this
    }

    fun settle(species: Species): Biome {
        this.speciesList.add(species)
        this.resources.setPopulation(species, 1.0)
        onBiomeProcess.notifyChange()
        return this
    }

    fun getStatusText(): String {
        val sb = StringBuilder("BiomeStatus: $name")
        sb.append("\n")
        sb.append(resources)
        speciesList.forEach {
            sb.append("\n")
            sb.append(getStatusText(it))
        }
        return sb.toString()
    }

    private fun getStatusText(species: Species): String {
        return species.name + ": " + (species.getPopulationIn(this) * 1E6).toShortDecimalStr() +
                " -> " + (species.process(this.resources).getPopulation(species) * 1E6).toShortDecimalStr()
    }

    fun getSpecies(): Array<Species> = resources.getSpecies()
}
