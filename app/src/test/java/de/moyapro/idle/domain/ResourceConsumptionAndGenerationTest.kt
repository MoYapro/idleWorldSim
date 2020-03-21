package de.moyapro.idle.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ResourceConsumptionAndGenerationTest {
    @Test
    fun generateDefault() {
        assertThat(Species().generationAndComsumption().evolutionPoints).isEqualTo(1.0)
    }

    @Test
    fun generateForSomeTime() {
        assertThat(Species().generationAndComsumption(2).evolutionPoints).isEqualTo(2.0)
    }

    @Test
    fun generateDependingOnNumberOfIndividuals() {
        assertThat(Species(individualsInMillons = 2.0).generationAndComsumption())
            .isEqualTo(Resources(evolutionPoints = 2.0, energy = -2000, water = -2000, minerals = -2000))
    }

    @Test
    fun generateSpeciesWithTraits() {
        val species:Species = Species().evolve(EvolutionBooster()).evolve(EvolutionBooster())
        assertThat(species.generationAndComsumption().evolutionPoints).isEqualTo(1.3225)
    }
}