package de.moyapro.idle.domain

data class Biome(
    val resources: Resources = Resources(),
    private val species: MutableCollection<Species> = mutableListOf()
) {
    fun getSpecies(): List<Species> {
        return species.toList()
    }

    fun generate(seconds: Int = 1): Biome {
        species.shuffled().forEach {
            val generatedResources = it.generateAndConsume(seconds)
            if(resources.hasShortage(generatedResources)) {
                it.die(seconds)
            } else {
                resources.add(generatedResources)
                it.grow()
            }
        }
        return this
    }

    fun settle(species: Species): Biome {
        this.species.add(species)
        return this
    }
}