package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
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
            )[species].populationSize
        ).isGreaterThan(1.0)
    }

    @Test
    fun die() {
        val species = defaultSpecies()
        assertThat(
            species.process(
                emptyResources().setPopulation(species, Population(1.0))
            )[species]
                .populationSize
        ).isLessThan(1.0)
    }
}
