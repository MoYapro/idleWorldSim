package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.skillTree.TreeOfLife
import de.moyapro.idleworldsim.domain.traits.Feature

object Game {


    private val treeOfLife = defaultTreeOfLife()

    private val biomes = mutableSetOf(Biome().settle(Species("Start here", autotrophic)))
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

    fun getEvolveOptions(): List<Feature> {
        return treeOfLife.getEvolvableFeatures(*selectedSpecies.features.toTypedArray()).toList()
    }


    val help: String = """
        S   Species     List species in selected biome - use number to select species
        E   Features    List available features for selected species - use number to evolve feature in currently selected species
        B   Biomes      List biomes - use number to select biomes
        H   HELP        Print this help
        Q   Quit        End the game
""".trimIndent()

}

private val autotrophic = Feature("Autotrophic")
private val photosynthesis = Feature("Photosynthesis")
private val vertebrate = Feature("Spinal Cord")
private val herbivore = Feature("Herbivore")
private val carnivore = Feature("Carnivore")
private val smallPlant = Feature("Small Plant")
private val largePlant = Feature("Large Plant")
private fun defaultTreeOfLife(): TreeOfLife<Feature> {
    return TreeOfLife.build(autotrophic) {
        branchInto(photosynthesis) {
            branchInto(smallPlant)
            branchInto(largePlant)
        }
        branchInto(vertebrate) {
            branchInto(herbivore)
            branchInto(carnivore)
        }
    }
}