package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Resource.*
import de.moyapro.idle.domain.consumption.Resources
import de.moyapro.idle.domain.consumption.emptyResources
import de.moyapro.idle.domain.traits.Meaty
import de.moyapro.idle.domain.traits.Predator
import de.moyapro.idle.domain.traits.oxygenConsumer
import de.moyapro.idle.domain.traits.sunlightConsumer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BiomeTest {
    @Test
    fun createBiome() {
        assertNotNull(Biome(), "Should create biome instance")
    }

    @Test
    fun defaultBiomeIsStable() {
        assertEquals(Biome(), Biome().process())
    }

    @Test
    fun biomeResourceUpdateAfterGenerating() {
        val biome = Biome().settle(defaultSpecies()).process()
        assertNotEquals(
            0,
            biome.resources[EvolutionPoints],
            "Species should change resources in biome"
        )
    }

    @Test
    fun speciesGrowAndDie() {
        val species = defaultSpecies()
        val biome = Biome().settle(species)
        val initialSpeciesSize = species.getPopulationIn(biome)
        biome.process()
        assertNotEquals(
            initialSpeciesSize,
            species.getPopulationIn(biome),
            "Should update speciesSize when generating in biome"
        )
    }

    @Test
    fun speciesConsumeWater() {
        val initialWaterLevel = 100_000.0
        val biome = Biome(resources = Resources().setQuantity(Water, initialWaterLevel))
            .settle(defaultSpecies())
            .process()
        assertThat(biome.resources[Water]).isLessThan(initialWaterLevel)

    }

    @Test
    fun speciesShouldShrinkOnResourceShortage() {
        val species1 = defaultSpecies()
        val species2 = defaultSpecies()
        val usualGrowthResult = Biome().settle(species1).process().resources.getPopulation(species1)
        val cappedGrowthResult = Biome(resources = Resources().setQuantity(Water, 0.0)).settle(species2).process().resources.getPopulation(species2)
        assertThat(cappedGrowthResult).isLessThan(usualGrowthResult)
    }

    @Test
    fun speciesShouldNotConsumeOnResourceShortage() {
        val initialResources = Resources(DoubleArray(values().size) { 0.0 })
        val resourcesAfterGeneration =
            Biome(resources = initialResources)
                .settle(defaultSpecies())
                .process()
                .resources
        assertThat(initialResources.quantities).containsExactly(*resourcesAfterGeneration.quantities)
    }

    @Test
    fun biomeStatusText() {
        val biomeName = "DefaultBiome${Math.random()}"
        val expectedBiomeStatus = """
            BiomeStatus: $biomeName
            Resources(evolution points=1.0, energy=999.0, water=999.0, minerals=999.0, oxygen=1000.0)
            Species1: 1.1M -> 1.21M
            Species2: 1.0M -> 1.1M
            """.trimIndent()
        val biome = Biome(biomeName).settle(defaultSpecies(name = "Species1")).process()
            .settle(defaultSpecies("Species2"))
        assertThat(biome.getStatusText()).isEqualTo(expectedBiomeStatus)
    }

    @Test
    fun speciesCanEatEachOther() {
        val initialResources = Resources(DoubleArray(values().size) { 2.0 })
        val predator = defaultSpecies("Predator")
        predator.evolve(Predator(Meaty))
        val prey = defaultSpecies("Prey").evolve(Meaty)
        val biome = Biome(resources = initialResources)
            .settle(predator)
            .settle(prey)
            .process()
        assertThat(predator.getPopulationIn(biome)).isEqualTo(1.1)
        assertThat(prey.getPopulationIn(biome)).isLessThan(1.1)
    }

    @Test
    fun speciesEatsAnotherSpecies() {
        val initialResources = Resources(DoubleArray(values().size) { 3.0 })
        val predator = defaultSpecies("Eater").evolve(Predator(Meaty))
        val prey = defaultSpecies("Food").evolve(Meaty)
        val uninvolved = defaultSpecies("Uninvolved")
        val biome = Biome(resources = initialResources)
            .settle(predator)
            .settle(prey)
            .settle(uninvolved)
            .process()
        assertThat(predator.getPopulationIn(biome)).isEqualTo(1.1)
        assertThat(prey.getPopulationIn(biome)).isLessThan(uninvolved.getPopulationIn(biome))
    }

    @Test
    fun biomeGeneratesResources() {
        val empty = emptyResources()
        val generation = Resources(DoubleArray(values().size) { 1.0 })
        val biome = Biome(resources = empty, generation = generation).process()
        assertThat(biome.resources[Minerals]).isGreaterThan(0.0)
    }

    @Test
    fun biomeWithEqualGenerationAndConsumptionIsStable() {
        val initial = Resources()
        val biomeGeneration = Resources(DoubleArray(values().size) { if (it == Energy.ordinal) 1.0 else 0.0 })
        val sunlightConsumer = Species("Plant").evolve(sunlightConsumer())
        val oxygenConsumer = Species("Animal").evolve(oxygenConsumer())
        val biome = Biome(resources = initial, generation = biomeGeneration)
            .settle(oxygenConsumer)
            .settle(sunlightConsumer)
            .process()
        val expectedResources = Resources(DoubleArray(values().size) { if (it == EvolutionPoints.ordinal) 0.0 else 1000.0 }) // 1EP from Biome and 1EP from Species
        assertThat(biome.resources.quantities).containsExactly(*expectedResources.quantities)
    }

    @Test
    fun biomeStoresResourcesGeneratedBySpecies() {
        val empty = emptyResources()
        val generation = Resources(DoubleArray(values().size) { 1.0 })
        generation[Oxygen] = 0.0 // biome does not create oxygen
        val species = defaultSpecies().evolve(sunlightConsumer())
        val biome = Biome(resources = empty, generation = generation).settle(species).process()
        assertThat(biome.resources[Oxygen]).`as`("Oxygen is generated by species with trait OxygenProducer").isGreaterThan(0.0)
    }
}
