package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Minerals
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class BiomeTest {
    @Test
    fun createBiome() {
        assertNotNull(Biome(), "Should create biome instance")
    }

    @Test
    fun defaultBiomeIsStable() {
        assertThat(Biome().population()).isEqualTo(Biome().process().population())
    }

    @Test
    fun addStaticResourceProdcuerToBiome() {
        Biome()
            .addResourceProducer(Species("Water").evolveTo(Feature(ProduceResource(Water))))
            .process()
    }

    @Test
    fun populationSizeIsChanging() {
        val species = defaultSpecies()
        val waterSource = BiomeFeature("Water", Feature(ProduceResource(Water)))
        val biome = Biome().settle(species).addResourceProducer(waterSource)
        val initialSpeciesSize = biome[species]
        biome.process()
        assertNotEquals(
            initialSpeciesSize,
            biome[species],
            "Should update speciesSize when generating in biome"
        )
    }


    @Test
    fun settle() {
        val species = Species("X")
        assertThat(Biome().settle(species, Population(3.0))[species]).isEqualTo(Population(3.0))
    }

    @Test
    fun settleMultiple() {
        val speciesX = Species("X")
        val speciesU = Species("U")
        val biome = Biome()
            .settle(speciesX, Population(3.0))
            .settle(speciesU, Population(99.0))
        assertThat(biome[speciesX]).isEqualTo(Population(3.0))
        assertThat(biome[speciesU]).isEqualTo(Population(99.0))
    }

    @Test
    fun addBasicResources() {
        val producer = Species(
            "WATER",
            listOf(Feature(ProduceResource(Water, Level(3))))
        )
        assertThat(Biome().settle(producer)).isNotNull
    }

    @Test
    fun consumerConsumesProducer() {
        val soil = BiomeFeature("soil", listOf(Feature(ProduceResource(Minerals))))
        val gras = Species("gras", listOf(Feature(ConsumerTrait(Minerals))))
        val biome = Biome()
            .place(soil)
            .settle(gras)
        val populationDifference: Map<TraitBearer, Population> = biome.getPopulationChanges()
        assertThat(populationDifference[soil]).isNull()
        assertThat(populationDifference[gras]?.populationSize ?: -1.0).isGreaterThan(0.0)


    }

    //
//    @Test
//    fun biomeStatusText() {
//        val biomeName = "DefaultBiome${Math.random()}"
//        val expectedBiomeStatus = """
//            BiomeStatus: $biomeName
//            Resources(Minerals=999.0, Oxygen=1000.0, EvolutionPoints=1.0, Energy=999.0, Water=999.0)[[Species[Species2 | grow=GrowthRate(rate=1.1), die=DeathRate(rate=1.0)]:1.0, Species[Species1 | grow=GrowthRate(rate=1.1), die=DeathRate(rate=1.0)]:1.1]]
//            Species1: 1.1M -> 1.21M
//            Species2: 1.0M -> 1.1M
//            """.trimIndent()
//        val biome = Biome(biomeName, Resources()).settle(defaultSpecies(name = "Species1")).process()
//            .settle(defaultSpecies("Species2"))
//        assertThat(biome.getStatusText()).isEqualTo(expectedBiomeStatus)
//    }
//
    @Test
    fun speciesCanEatEachOther() {
        val predator = Species("Predator", Feature(Predator(Meaty())))
        val prey = Species("Prey", Feature(Meaty()))
        val biome = Biome()
            .settle(predator)
            .settle(prey)
            .process()
        assertThat(biome[predator].populationSize).isGreaterThan(1.0)
        assertThat(biome[prey].populationSize).isLessThan(1.1)
    }

    @Test
    fun predatorEatsOnlySomeSpecies() {
        val predator = Species("Eater", Feature(Predator(Meaty()), NeedResource(Water), NeedResource(Minerals)))
        val prey = Species("Food", Feature(Meaty()))
        val uninvolved = Species("Uninvolved")
        val biome = Biome()
            .settle(predator, Population(1))
            .settle(prey, Population(100))
            .settle(uninvolved, Population(100))
            .process()
        assertThat(biome[predator].populationSize).isGreaterThan(1.0)
        assertThat(biome[prey].populationSize).isLessThan(biome[uninvolved].populationSize)
    }


    @Test
    fun predatorEatsUntilSatisfied() {
        val predator = Species("Eater", Feature(Predator(Meaty()), NeedResource(Water), NeedResource(Minerals)))
        val prey = Species("Tasty Food", Feature(Meaty(Level(10)), ProduceResource(Water), ProduceResource(Minerals)))
        val lesserPrey = Species("Food", Feature(Meaty(), ProduceResource(Minerals)))
        val biome = Biome()
            .settle(predator, Population(1))
            .settle(prey, Population(1000))
            .settle(lesserPrey, Population(1000))
            .process()
        assertThat(biome[predator].populationSize).isGreaterThan(1.0)
        assertThat(biome[prey].populationSize).isLessThan(biome[lesserPrey].populationSize)
    }

    @Test
    fun evolve() {
        val originalSpecies = Species("x")
        val biome = Biome().settle(originalSpecies)
        assertThat(biome.species().size).isEqualTo(1)
        val newSpecies = biome.evolve(originalSpecies, Feature.sunlightConsumer())
        assertThat(biome.species().size).isEqualTo(2)
        assertThat(biome.species()).contains(originalSpecies)
        assertThat(biome.species()).contains(newSpecies)
    }

    @Test
    fun speciesIsAddedToBiomeTwice_ShouldJustIncreasePopulation() {
        val someSpecies = Species("X", Feature(Meaty()))
        val sameSpecies = Species("X", Feature(Meaty()))
        val population = Biome()
            .settle(someSpecies)
            .settle(sameSpecies)
            .population()
        assertThat(population.size).isEqualTo(1)
        assertThat(population[someSpecies]?.populationSize).isEqualTo(2.0)
        assertThat(population[sameSpecies]?.populationSize).isEqualTo(2.0)
    }

    @Test
    fun speciesShouldBeCappedByAvailableResources() {
        val soil =
            BiomeFeature("Soil", Feature(ProduceResource(Minerals, Level(10)), ProduceResource(Water, Level(10))))
        val air = BiomeFeature(
            "Air",
            Feature(ProduceResource(ResourceType.Oxygen, Level(10)), ProduceResource(ResourceType.Carbon, Level(10)))
        )
        val grass = Species(
            "Grass",
            Feature(
                NeedResource(Minerals),
                NeedResource(Water),
                NeedResource(ResourceType.Carbon),
                ConsumerTrait(Minerals),
                ConsumerTrait(Water),
                ConsumerTrait(ResourceType.Carbon)
            )
        )
        var biome = Biome()
            .addResourceProducer(soil)
            .addResourceProducer(air)
            .settle(grass)
        repeat(1000) {
            biome = biome.process()
        }
        biome = biome.process()

        assertThat(biome[grass].populationSize).isLessThan(10.0)


    }

}
