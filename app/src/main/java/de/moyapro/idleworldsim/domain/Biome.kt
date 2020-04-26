package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.util.applyTo
import de.moyapro.idleworldsim.util.toShortDecimalStr
import java.util.*

data class Biome(
    val name: String = "DefaultBiome",
    var resources: Resources = Resources(),
    var generation: Resources = emptyResources(),
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
        this.resources += generation
        this.resources = speciesList
            .shuffled()
            .applyTo(resources, Species::process)
        onBiomeProcess.notifyChange()
        return this
    }

    fun settle(species: Species): Biome {
        this.speciesList.add(species)
        this.resources.setPopulation(species, Population(1.0))
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
        return species.name + ": " + (species.getPopulationIn(this)).toShortDecimalStr(1E6) +
                " -> " + (species.process(this.resources).getPopulation(species)).toShortDecimalStr(1E6)
    }

    fun getSpecies(): Array<Species> = resources.getSpecies()
}
