package de.moyapro.idle.domain

import de.moyapro.idle.util.toShortDecimalStr

data class Biome(
    val name: String = "DefaultBiome",
    var resources: Resources = Resources(),
    private val speciesList: MutableCollection<Species> = mutableListOf()
) {
    fun process(): Biome {
        speciesList.shuffled().forEach {
            val leftovers = it.process(this.resources)
            this.resources = leftovers
        }
        return this
    }

    fun settle(species: Species): Biome {
        this.speciesList.add(species)
        this.resources.setPopulation(species, 1.0)
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
        return species.name + ": " + (species.getPopulationIn(this)*1E6).toShortDecimalStr() +
                " -> " + (species.process(this.resources).getPopulation(species)*1E6).toShortDecimalStr()
    }
}
