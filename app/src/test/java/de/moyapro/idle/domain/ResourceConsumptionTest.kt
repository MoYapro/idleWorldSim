package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Resource.EvolutionPoints
import de.moyapro.idle.domain.consumption.Resources
import de.moyapro.idle.domain.traits.EvolutionBooster
import de.moyapro.idle.domain.util.defaultOffset
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ResourceConsumptionTest {
    @Test
    fun generateDefault() {
        assertThat(defaultSpecies().process(Resources(doubleArrayOf(1.0, 1.0, 1.0, 1.0)))[EvolutionPoints]).isEqualTo(1.0)
    }

    @Test
    fun generateDependingOnNumberOfIndividuals() {
        val species = defaultSpecies()
        assertThat(species.process(Resources(doubleArrayOf(0.0, 3.0, 3.0, 3.0)).setPopulation(species, 2.0)))
            .isEqualTo(Resources(doubleArrayOf(2.0, 1.0, 1.0, 1.0)).setPopulation(species, 2.2))
    }

    @Test
    fun generateSpeciesWithTraits() {
        val species = defaultSpecies().evolve(EvolutionBooster).evolve(EvolutionBooster)
        assertThat(species.process(Resources(doubleArrayOf(0.0, 3.0, 3.0, 3.0)).setPopulation(species, 1.0))[EvolutionPoints]).isEqualTo(1.15, defaultOffset())
    }
}
