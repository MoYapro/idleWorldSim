package de.moyapro.idle.domain

data class Biome(
    val resources: Resources = Resources(),
    val species: MutableCollection<Species> = mutableListOf()
) {
    fun generate(seconds: Int = 1): Biome {
        species.forEach{
            resources.add(it.generate(seconds))
        }

        return this
    }
}