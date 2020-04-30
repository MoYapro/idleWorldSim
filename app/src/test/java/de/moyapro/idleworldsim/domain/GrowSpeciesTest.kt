package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.valueObjects.Population
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpeciesGrowAndDieTest {

    @Test
    fun grow() {
        val species = defaultSpecies()
        assertThat(
            species.process(
                Resources().setPopulation(species, Population(1.0))
            ).getPopulation(species)
        ).isEqualTo(1.1)
    }

    @Test
    fun die() {
        val species = defaultSpecies()
        assertThat(
            species.process(
                (Resources() * 0.0)
                    .setPopulation(species, Population(1.0))
            ).getPopulation(species)
                .populationSize
        ).isLessThan(1.0)
    }
}
