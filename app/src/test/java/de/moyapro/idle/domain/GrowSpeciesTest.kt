package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpeciesGrowAndDieTest {

    @Test
    fun grow() {
        val species = defaultSpecies()
        assertThat(
            species.process(
                Resources().setPopulation(species, 1.0)
            ).getPopulation(species)
        ).isEqualTo(1.1)
    }

    @Test
    fun die() {
        val species = defaultSpecies()
        assertThat(
            species.process(
                (Resources() * 0.0)
                    .setPopulation(species, 1.0)
            ).getPopulation(species)
        ).isLessThan(1.0)
    }
}
