package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.defaultSpecies
import de.moyapro.idleworldsim.domain.valueObjects.Population
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SpeciesGrowAndDieTest {

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
