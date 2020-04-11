package de.moyapro.idle.domain

import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.consumption.Resource.EvolutionPoints
import de.moyapro.idleworldsim.domain.consumption.Resource.Water
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.defaultSpecies
import de.moyapro.idleworldsim.domain.traits.Predator
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
        val initialResources = Resources(doubleArrayOf(0.0, 10.0, -1.0, 10.0))
        val resourcesAfterGeneration =
            Biome(resources = initialResources)
                .settle(defaultSpecies())
                .process()
                .resources
        assertThat(initialResources).isEqualTo(resourcesAfterGeneration)
    }

    @Test
    fun biomeStatusText() {
        val biomeName = "DefaultBiome${Math.random()}"
        val expectedBiomeStatus = """
            BiomeStatus: $biomeName
            Resources(evolution points=1.0, energy=999.0, water=999.0, minerals=999.0)
            Species1: 1.1M -> 1.21M
            Species2: 1.0M -> 1.1M
            """.trimIndent()
        val biome = Biome(biomeName).settle(defaultSpecies(name = "Species1")).process()
            .settle(defaultSpecies("Species2"))
        assertThat(biome.getStatusText()).isEqualTo(expectedBiomeStatus)
    }

    @Test
    fun speciesCanEatEachOther() {
        val initialResources = Resources(doubleArrayOf(0.0, 2.0, 2.0, 2.0))
        val predator = defaultSpecies("Predator")
        val prey = defaultSpecies("Prey")
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
        val initialResources = Resources(doubleArrayOf(0.0, 3.0, 3.0, 3.0))
        val predator = defaultSpecies("Eater")
        val prey = defaultSpecies("Food")
        val uninvolved = defaultSpecies("Uninvolved")
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
