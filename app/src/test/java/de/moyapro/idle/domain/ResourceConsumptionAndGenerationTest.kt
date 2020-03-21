package de.moyapro.idle.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ResourceConsumptionAndGenerationTest {
    @Test
    fun generateDefault() {
        assertEquals(1.0, Species().generationAndComsumption().evolutionPoints, "Should generate in default settings")
    }

    @Test
    fun generateForSomeTime() {
        assertEquals(2.0, Species().generationAndComsumption(2).evolutionPoints, "Should generate for multiple seconds")
    }

    @Test
    fun generateDependingOnNumberOfIndividuals() {
        assertThat(Resources(evolutionPoints = 2.0, energy = -2, water = -2, minerals = -2))
            .isEqualTo(Species(individualsInMillons = 2.0).generationAndComsumption())
    }

    @Test
    fun generateSpeciesWithTraits() {
        val species:Species = Species().evolve(Trait()).evolve(Trait())
        assertEquals(1.3225, species.generationAndComsumption().evolutionPoints, .000001, "Should calculate generation including trait")
    }
}