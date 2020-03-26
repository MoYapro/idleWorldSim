package de.moyapro.idle.domain

data class Biome(
    val name: String = "DefaultBiome",
    var resources: Resources = Resources(),
    private val species: MutableCollection<Species> = mutableListOf()
) {
    fun getSpecies(): List<Species> {
        return species.toList()
    }

    fun generate(seconds: Int = 1): Biome {
        species.shuffled().forEach {
            val generatedResources = it.generationAndComsumption(seconds, it)
            if (resources.canProvide(generatedResources)) {
                resources = resources.plus(generatedResources)
                it.grow()
            } else {
                println("die")
                it.die(seconds)
            }
        }
        return this
    }

    fun settle(species: Species): Biome {
        this.species.add(species)
        return this
    }

    fun getStatusText(): String {
        val sb = StringBuilder("BiomeStatus: $name")
        sb.append("\n")
        sb.append(resources)
        species.forEach {
            sb.append("\n")
            sb.append(it.getStatusText())
        }
        return sb.toString()
    }
}
