package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.BiomeFeature
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.consumption.FoodChainEdge
import de.moyapro.idleworldsim.domain.skillTree.TreeOfLife
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

object Game {

    var running = false
    private val treeOfLife = defaultTreeOfLife()
    private val biomes = createStartingBiomes()
    var selectedBiome = biomes.values.first()
    var selectedSpecies: Species = selectedBiome.species().first()

    fun runSimulation() {
        running = true
        GlobalScope.launch {
            while (running) {
                delay(1000)
                if (running)
                    biomes.forEach { it.value.process() }
            }
        }
    }

    fun stopSimulation() {
        running = false
    }

    fun biomesList(): List<Biome> {
        return biomes.values.toList()
    }

    fun createBiome(name: String) = addBiome(Biome(name))

    private fun addBiome(biome: Biome) = biome.also { biomes[it.id] = it }

    fun getBiome(id: UUID) = biomes[id]

    fun selectBiome(biome: Biome) {
        selectedBiome = biome
    }

    fun selectSpecies(species: Species) {
        selectedSpecies = species
    }

    fun getEvolveOptions(): List<Feature> {
        return treeOfLife.getEvolvableFeatures(*selectedSpecies.features.toTypedArray()).toList()
    }

    fun getEvolveOptions(species: Species): List<Feature> {
        return treeOfLife.getEvolvableFeatures(*species.features.toTypedArray()).toList()
    }

    fun speciesRelations(): Iterable<FoodChainEdge> {
        return selectedBiome.getRelations()
    }

    fun getTraitsOfSelectedSpecies(): List<Trait> = selectedSpecies.traits().toList()
    fun getSpecies(speciesName: String): Species {
        return biomes.values
            .map { it.species().filter { species -> species.name == speciesName } }
            .flatten()
            .first() // assuming names are unique
    }


    val help: String = """
        S   Species     List species in selected biome - use number to select species
        R   Relations   List food chain relations between species (to be implemented)
        T   Traits      List traits of the current species
        E   Features    List available features for selected species - use number to evolve feature in currently selected species
        B   Biomes      List biomes - use number to select biomes
        H   HELP        Print this help
        Q   Quit        End the game
""".trimIndent()

}

private val autotrophic = Feature("Autotrophic", ConsumerTrait(Water), ConsumerTrait(Minerals), NeedResource(Water), NeedResource(Minerals))
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

private fun createStartingBiomes() = mutableMapOf<UUID, Biome>().apply {
    val biome = Biome("initial biome")
        .settle(Species("Origin of life", autotrophic))
        .settle(Species("Algae", photosynthesis))
        .settle(Species("Small Plant", photosynthesis, smallPlant))
        .settle(Species("Large Plant", photosynthesis, largePlant))
        .settle(Species("Stone eater", autotrophic, vertebrate))
        .settle(Species("Herbivore", vertebrate, herbivore))
        .settle(Species("Carnivore", vertebrate, carnivore))
        .addResourceProducer(
            BiomeFeature(
                "Ocean", Feature(
                    Size(10),
                    ProduceResource(Water, Level(99)),
                    ProduceResource(Minerals, Level(1)),
                    ProduceResource(Carbon, Level(1))
                )
            )
        )
        .addResourceProducer(
            BiomeFeature(
                "Ocean Floor", Feature(
                    Size(3),
                    ProduceResource(Minerals, Level(25))
                )
            )
        )
    this[biome.id] = biome
}
