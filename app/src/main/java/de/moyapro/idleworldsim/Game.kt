package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.BiomeFeature
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.skillTree.TreeOfLife
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*

object Game {


    private val treeOfLife = defaultTreeOfLife()

    private val biomes = mutableSetOf(createStatingBiome())

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
private val photosynthesis = Feature("Photosynthesis", ProduceResource(Oxygen), NeedResource(Water), NeedResource(Carbon))
private val vertebrate = Feature("Spinal Cord")
private val herbivore = Feature("Herbivore", Meaty(), Predator(ProduceResource(Oxygen)))
private val carnivore = Feature("Carnivore", Predator(Meaty()))
private val smallPlant = Feature("Small Plant", ProduceResource(Oxygen, Level(2)), NeedResource(Water, Level(2)), NeedResource(Carbon))
private val largePlant = Feature("Large Plant", ProduceResource(Oxygen, Level(10)), NeedResource(Water, Level(5)), NeedResource(Carbon, Level(5)))
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

private fun createStatingBiome() =
    Biome()
        .settle(Species("Start here", autotrophic))
        .addResourceProducer(
            BiomeFeature(
                "Ocean", Feature(
                    Size(1000),
                    ProduceResource(Water, Level(99)),
                    ProduceResource(Minerals, Level(1))
                )
            )
        )
        .addResourceProducer(
            BiomeFeature(
                "Ocean Floor", Feature(
                    Size(100),
                    ProduceResource(Minerals, Level(25))
                )
            )
        )
