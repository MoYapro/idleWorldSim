package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.two.defaultSpecies
import de.moyapro.idleworldsim.domain.valueObjects.Population
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpeciesGrowAndDieTest {

    @Test
    fun grow() {
        val species = defaultSpecies()
        assertThat(
            species.consume(
                Population(1.0), Resources()
            ).populationSize
        ).isGreaterThan(1.0)
    }

    @Test
    fun die() {
        val species = defaultSpecies()
        assertThat(
            species.consume(
                Population(1.0), emptyResources()
            ).populationSize
        ).isLessThan(1.0)
    }
}
