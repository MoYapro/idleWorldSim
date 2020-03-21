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
        assertEquals(Biome(), Biome().generate())
    }

    @Test
    fun biomeResourceUpdateAfterGenerating() {
        val biome = Biome().settle(Species()).generate()
        assertNotEquals(
            0,
            biome.resources.evolutionPoints,
            "Species should change resources in biome"
        )
    }

    @Test
    fun speciesGrowAndDie() {
        val biome = Biome().settle(Species())
        val initialSpeciesSize = biome.getSpecies()[0]
        biome.generate()
        assertNotEquals(
            initialSpeciesSize,
            biome.getSpecies()[0].individualsInMillons,
            "Should update speciesSize when generating in biome"
        )
    }

    @Test
    fun speciesConsumeWater() {
        val initialWaterLevel = 100_000
        val biome = Biome(resources = Resources(water = initialWaterLevel))
            .settle(Species())
            .generate()
        assertThat(biome.resources.water).isLessThan(initialWaterLevel)

    }

    @Test
    fun speciesShouldShrinkOnResourceShortage() {
        val usualGrothResult = Biome(resources = Resources(water = 100_000)).settle(Species()).generate().getSpecies()[0].individualsInMillons
        val cappedGrothResult = Biome(resources = Resources(water = 0)).settle(Species()).generate().getSpecies()[0].individualsInMillons
        assertThat(cappedGrothResult).isLessThan(usualGrothResult)
    }

    @Test
    fun foo() {
        val cappedGrothResult = Biome(resources = Resources(water = 0)).settle(Species()).generate().getSpecies()[0].individualsInMillons
    }

    @Test
    fun speciesShouldNotConsumeOnResourceShortage() {
        val initialResources = Resources(energy = 10, water = -1, minerals = 10)
        val resourcesAfterGeneration =
            Biome(resources = initialResources)
                .settle(Species())
                .generate()
                .resources
        assertThat(initialResources).isEqualTo(resourcesAfterGeneration)
    }

    @Test
    fun biomeStatusText() {
        val biomeName = "DefaultBiome${Math.random()}"
        val expectedBiomeStatus = """
            BiomeStatus: $biomeName
            Resources(evolutionPoints=1.0, energy=0, water=0, minerals=0)
            Species1: 1.1M -> Resources(evolutionPoints=1.1, energy=-1100, water=-1100, minerals=-1100)
            Species2: 1.0M -> Resources(evolutionPoints=1.0, energy=-1000, water=-1000, minerals=-1000)
            """.trimIndent()
        val biome = Biome(biomeName).settle(Species(name = "Species1")).generate()
            .settle(Species("Species2"))
        assertThat(biome.getStatusText()).isEqualTo(expectedBiomeStatus)
    }


}