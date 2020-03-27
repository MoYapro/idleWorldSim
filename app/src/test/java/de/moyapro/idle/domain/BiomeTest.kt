package de.moyapro.idle.domain

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
        val biome = Biome().settle(Species()).process()
        assertNotEquals(
            0,
            biome.resources.evolutionPoints,
            "Species should change resources in biome"
        )
    }

    @Test
    fun speciesGrowAndDie() {
        val species = Species()
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
        val biome = Biome(resources = Resources(water = initialWaterLevel))
            .settle(Species())
            .process()
        assertThat(biome.resources.water).isLessThan(initialWaterLevel)

    }

    @Test
    fun speciesShouldShrinkOnResourceShortage() {
        val species1 = Species()
        val species2 = Species()
        val usualGrowthResult = Biome(resources = Resources(water = 100_000.0)).settle(species1).process().resources.getPopulation(species1)
        val cappedGrowthResult = Biome(resources = Resources(water = 0.0)).settle(species2).process().resources.getPopulation(species2)
        assertThat(cappedGrowthResult).isLessThan(usualGrowthResult)
    }

    @Test
    fun speciesShouldNotConsumeOnResourceShortage() {
        val initialResources = Resources(energy = 10.0, water = -1.0, minerals = 10.0)
        val resourcesAfterGeneration =
            Biome(resources = initialResources)
                .settle(Species())
                .process()
                .resources
        assertThat(initialResources).isEqualTo(resourcesAfterGeneration)
    }

    @Test
    fun biomeStatusText() {
        val biomeName = "DefaultBiome${Math.random()}"
        val expectedBiomeStatus = """
            BiomeStatus: $biomeName
            Resources(evp=1.0, nrg=999.0, h20=999.0, ore=999.0)
            Species1: 1.1M -> 1.21M
            Species2: 1.0M -> 1.1M
            """.trimIndent()
        val biome = Biome(biomeName).settle(Species(name = "Species1")).process()
            .settle(Species("Species2"))
        assertThat(biome.getStatusText()).isEqualTo(expectedBiomeStatus)
    }

    @Test
    fun speciesCanEatEachOther() {
        val initialResources = Resources(energy = 2.0, water = 2.0, minerals = 2.0)
        val predator = Species("Predator")
        val prey = Species("Prey")
        predator.evolve(Predator(prey))
        val biome = Biome(resources = initialResources)
            .settle(predator)
            .settle(prey)
            .process()
        assertThat(predator.getPopulationIn(biome)).isEqualTo(1.1)
        assertThat(prey.getPopulationIn(biome)).isLessThan(1.1)
    }

    @Test
    fun speciesEatsAnotherSpecies() {
        val initialResources = Resources(energy = 3.0, water = 3.0, minerals = 3.0)
        val predator = Species("Eater")
        val prey = Species("Food")
        val uninvolved = Species("Uninvolved")
        predator.evolve(Predator(prey))
        val biome = Biome(resources = initialResources)
            .settle(predator)
            .settle(prey)
            .settle(uninvolved)
            .process()
        assertThat(predator.getPopulationIn(biome)).isEqualTo(1.1)
        assertThat(prey.getPopulationIn(biome)).isLessThan(uninvolved.getPopulationIn(biome))
    }
}
