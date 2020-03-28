package de.moyapro.idle.domain

import de.moyapro.idle.domain.util.defaultOffset
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test

internal class ResourceConsumptionTest {
    @Test
    fun generateDefault() {
        assertThat(Species().process(Resources(1.0, 1.0, 1.0, 1.0)).evolutionPoints).isEqualTo(1.0)
    }

    @Test
    fun generateDependingOnNumberOfIndividuals() {
        val species = Species()
        assertThat(species.process(Resources(0.0, 3.0, 3.0, 3.0).setPopulation(species, 2.0)))
            .isEqualTo(Resources(evolutionPoints = 2.0, energy = 1.0, water = 1.0, minerals = 1.0).setPopulation(species, 2.2))
    }

    @Test
    fun generateSpeciesWithTraits() {
        val species= Species().evolve(EvolutionBooster()).evolve(EvolutionBooster())
        assertThat(species.process(Resources(0.0, 3.0, 3.0, 3.0).setPopulation(species, 1.0)).evolutionPoints).isEqualTo(1.3225, defaultOffset())
    }
}
