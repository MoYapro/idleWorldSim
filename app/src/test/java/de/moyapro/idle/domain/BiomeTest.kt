package de.moyapro.idle.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

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
        assertNotEquals(initialSpeciesSize, biome.getSpecies()[0].milionsOfIndividuals, "Should update speciesSize when generating in biome")
    }

    @Test
    fun speciesConsumeWater() {
        val initialWaterLevel = 100_000
        val biome = Biome(Resources(water = initialWaterLevel)).settle(Species()).generate()
        assertThat(biome.resources.water).isLessThan(initialWaterLevel)

    }

    @Test
    fun speciesShouldShrinkOnResourceShortage() {
        val usualGrothResult = Biome(Resources(water = 100_000)).settle(Species()).generate().getSpecies()[0].milionsOfIndividuals
        val cappedGrothResult = Biome(Resources(water = 0)).settle(Species()).generate().getSpecies()[0].milionsOfIndividuals
        assertThat(cappedGrothResult).isLessThan(usualGrothResult)
    }


}