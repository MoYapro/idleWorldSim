package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.valueObjects.AquireSkill
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
import de.moyapro.idleworldsim.util.toShortDecimalStr
import java.util.*

data class Biome(
    val name: String = "DefaultBiome",
    var resources: Resources = emptyResources(),
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
//        calculate Map<Species, Map<ResourceType,  AquireSkill>> to distribute available resources
//        determin Map<Species, Resources> how much is the species able to consume

        val availableResourcePerSpecies: Map<Species, Resources> = getAvailableResourcesPerSpecies()
        speciesList
            .forEach { it.process(availableResourcePerSpecies[it] ?: emptyResources()) }
        onBiomeProcess.notifyChange()
        return this
    }

    fun getAvailableResourcesPerSpecies(): Map<Species, Resources> {
        return this.resources.populations.entries
            .map { (species, population) ->
                Pair(
                    species,
                    species.needsPerIndividual() * population
                )
            }.associate { it }
    }

    private operator fun get(species: Species, resourceType: ResourceType): AquireSkill {
        return AquireSkill(1.0)
    }

    fun settle(species: Species, population: Population = Population(1.0)): Biome {
        this.speciesList.add(species)
        this.resources.setPopulation(species, population)
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
                " -> " + (species.process(this.resources).get(species)).toShortDecimalStr(1E6)
    }

    fun getSpecies() = resources.getSpecies()
}
