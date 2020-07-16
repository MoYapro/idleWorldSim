package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.Meaty

object Game {


    private val biomes = mutableSetOf(Biome().settle(Species("Start here")))
    var selectedBiome = biomes.first()
    var selectedSpecies: Species = selectedBiome.species().first()
    fun getStatusText(): String {
        return biomes.map { it.population() }.toString()
    }

    fun process() {
        TODO("Not yet implemented")
    }

    fun biomes(): List<Biome> {
        return biomes.toList()
    }

    fun selectBiome(biome: Biome) {
        selectedBiome = biome
    }

    fun selectSpecies(species: Species) {
        selectedSpecies = species
    }

    fun availableFeatures(): List<Feature> {
        return listOf(Feature("MMMM", Meaty()))
    }


    val help: String = """
        S   Species     List species in selected biome - use number to select species
        E   Features    List available features for selected species - use number to evolve feature in currently selected species
        B   Biomes      List biomes - use number to select biomes
        H   HELP        Print this help
        Q   Quit        End the game
""".trimIndent()

}