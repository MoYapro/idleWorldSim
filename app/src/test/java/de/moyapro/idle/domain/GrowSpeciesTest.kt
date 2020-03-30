package de.moyapro.idle.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpeciesGrowAndDieTest {

    @Test
    fun grow() {
        val species = DefaultSpecies()
        assertThat(
            species.process(
                Resources().setPopulation(species, 1.0)
            ).getPopulation(species)
        ).isEqualTo(1.1)
    }

    @Test
    fun die() {
        val species = DefaultSpecies()
        assertThat(
            species.process(
                (Resources() * 0.0)
                    .setPopulation(species, 1.0)
            ).getPopulation(species)
        ).isLessThan(1.0)
    }
}
