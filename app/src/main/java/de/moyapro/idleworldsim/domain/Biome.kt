package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.traits.AquireResource
import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.values
import de.moyapro.idleworldsim.util.toShortDecimalStr
import java.lang.Integer.max
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

        val availableResourcePerSpecies: Map<Species, Resources> = getAquiredResourcesPerSpecies()
        speciesList
            .forEach {
                val available = availableResourcePerSpecies[it] ?: emptyResources()
                available.populations = resources.populations
                val leftovers = it.process(available)
                val used = available - leftovers
                resources -= used
            }
        onBiomeProcess.notifyChange()
        return this
    }

    fun getAquiredResourcesPerSpecies(): Map<Species, Resources> {
        val resultMap = resources.getSpecies().associateWithTo(mutableMapOf()) { emptyResources() }
        for (resourceType in values()) {
            val needPerSpecies: Map<Species, Resource> = resources.populations
                .filter { it.key.hasTrait(ConsumerTrait(resourceType)) }
                .map { (species, population) -> Pair(species, species.needsPerIndividual()[resourceType] * population) }
                .associate { it }
            val totalAvailable: Resource = Resource(resourceType, 10)
            val totalNeed = Resource(resourceType, needPerSpecies.values.sumByDouble { it.amount })
            if (totalNeed < totalAvailable) {
                distributeWithSurplus(resultMap, needPerSpecies)
            }
            distributeWithShortage(resultMap, resourceType, totalAvailable)
        }
        return resultMap
    }

    private fun distributeWithShortage(resultMap: MutableMap<Species, Resources>, resourceType: ResourceType, totalAvailable: Resource) {
        val totalAquireValue = max(1, resources.populations.keys.map { it[AquireResource(resourceType)].level }.max() ?: 1)
        val relativeAquireValuePerSpecies = resources.populations.keys
            .map { Pair(it, calculateRelativeAquireValue(totalAquireValue - it[AquireResource(resourceType)].level)) }
        relativeAquireValuePerSpecies
            .forEach { (s, relativeAquired) -> resultMap[s]!![resourceType] = totalAvailable * (relativeAquired / totalAquireValue) }
    }

    private fun distributeWithSurplus(resultMap: MutableMap<Species, Resources>, needPerSpecies: Map<Species, Resource>) {
        needPerSpecies.forEach { (s, r) -> resultMap[s]!![r.resourceType] = r }
    }

    /**
     * This determins how much falloff there is when species is 'rank' levels behind the best
     */
    private fun calculateRelativeAquireValue(rank: Int) = 1.0 / ((rank) * 2)

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
