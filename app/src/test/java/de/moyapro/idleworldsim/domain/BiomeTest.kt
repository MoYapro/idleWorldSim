package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.ProduceResource
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
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
        val waterSource = Species("Water", Feature(ProduceResource(Water)))
        val biome = Biome().settle(species).settle(waterSource)
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
        val species: Species = Species("X")
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
        val soil = BiomeFeature("soil", listOf(Feature(ProduceResource(ResourceType.Minerals))))
        val gras = Species("gras", listOf(Feature(ConsumerTrait(ResourceType.Minerals))))
        val biome = Biome()
            .place(soil)
            .settle(gras)
        val populationDifference: Map<TraitBearer, Population> = biome.getPopulationChanges()
        assertThat(populationDifference[soil]?.populationSize).isEqualTo(0.0)
        assertThat(populationDifference[gras]?.populationSize).isGreaterThan(0.0)


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
//    @Test
//    fun speciesCanEatEachOther() {
//        val initialResources = Resources(DoubleArray(values().size) { 2.0 })
//        val predator = defaultSpecies("Predator")
//        predator.evolve(Predator(Meaty))
//        val prey = defaultSpecies("Prey").evolve(Meaty)
//        val biome = Biome(resources = initialResources)
//            .settle(predator)
//            .settle(prey)
//            .process()
//        assertThat(predator.getPopulationIn(biome).populationSize).isGreaterThan(1.0)
//        assertThat(prey.getPopulationIn(biome).populationSize).isLessThan(1.1)
//    }
//
//    @Test
//    fun speciesEatsAnotherSpecies() {
//        val predator = defaultSpecies("Eater").evolve(Predator(Meaty))
//        val prey = defaultSpecies("Food").evolve(Meaty)
//        val uninvolved = defaultSpecies("Uninvolved")
//        val biome = Biome("Earth", Resources())
//            .settle(predator)
//            .settle(prey)
//            .settle(uninvolved)
//            .process()
//        assertThat(predator.getPopulationIn(biome).populationSize).isGreaterThan(1.0)
//        assertThat(prey.getPopulationIn(biome).populationSize).isLessThan(uninvolved.getPopulationIn(biome).populationSize)
//    }
//
//    @Test
//    fun biomeGeneratesResources() {
//        val empty = Resources()
//        val generation = Resources(DoubleArray(values().size) { 1.0 })
//        val biome = Biome(resources = empty, generation = generation).process()
//        assertThat(biome.resources[Minerals].amount).isGreaterThan(0.0)
//    }
//
//    @Test
//    fun biomeWithEqualGenerationAndConsumptionIsStable() {
//        val initial = Resources()
//        val generation = Resources(DoubleArray(values().size) { 1.0 })
//        val species = defaultSpecies()
//        val biome = Biome(resources = initial, generation = generation).settle(species).process()
//        val expectedResources = Resources(DoubleArray(values().size) { if (it == EvolutionPoints.ordinal) 2.0 else 1000.0 }) // 1EP from Biome and 1EP from Species
//        expectedResources[Oxygen] = Resource(Oxygen, 1001.0)
//        assertThat(biome.resources.quantities.entries).containsExactlyInAnyOrder(*expectedResources.quantities.entries.toTypedArray())
//    }
//
//    @Test
//    fun biomeStoresResourcesGeneratedBySpecies() {
//        val empty = Resources()
//        val generation = Resources(DoubleArray(values().size) { 1.0 })
//        generation[Oxygen] = Resource(Oxygen, 0.0) // biome does not create oxygen
//        val species = defaultSpecies().evolve(Feature.sunlightConsumer())
//        val biome = Biome(resources = empty, generation = generation).settle(species).process()
//        assertThat(biome.resources[Oxygen].amount).`as`("Oxygen is generated by species with trait OxygenProducer").isGreaterThan(0.0)
//    }
}