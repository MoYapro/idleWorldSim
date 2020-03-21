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
            biome.getSpecies()[0].milionsOfIndividuals,
            "Should update speciesSize when generating in biome"
        )
    }

    @Test
    fun speciesConsumeWater() {
        val initialWaterLevel = 100_000
        val biome =
            Biome(resources = Resources(water = initialWaterLevel)).settle(Species()).generate()
        assertThat(biome.resources.water).isLessThan(initialWaterLevel)

    }

    @Test
    fun speciesShouldShrinkOnResourceShortage() {
        val usualGrothResult =
            Biome(resources = Resources(water = 100_000)).settle(Species()).generate()
                .getSpecies()[0].milionsOfIndividuals
        val cappedGrothResult = Biome(resources = Resources(water = 0)).settle(Species()).generate()
            .getSpecies()[0].milionsOfIndividuals
        assertThat(cappedGrothResult).isLessThan(usualGrothResult)
    }

    @Test
    fun speciesShouldNotConsumeOnResourceShortage() {
        val initialWaterLevel = 100_000
        val newWaterLevel =
            Biome(resources = Resources(water = 0)).settle(Species()).generate().resources.water
        assertThat(initialWaterLevel).isEqualTo(newWaterLevel)
    }

    @Test
    fun biomeStatusText() {
        val biomeName = "DefaultBiome${Math.random()}"
        val expectedBiomeStatus = """
            BiomeStatus: $biomeName
            Resources(evolutionPoints=1.0, energy=1000, water=999, minerals=1000)
            Species1: 1.1M -> Resources(evolutionPoints=1.1, energy=-1, water=-1, minerals=-1)
            Species2: 1.0M -> Resources(evolutionPoints=1.0, energy=-1, water=-1, minerals=-1)
            """.trimIndent()
        val biome = Biome(biomeName).settle(Species(name = "Species1")).generate()
            .settle(Species("Species2"))
        assertThat(biome.getStatusText()).isEqualTo(expectedBiomeStatus)
    }


}