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
        val initialPopulation = Population(1.0)
        species.consume(
            initialPopulation, Resources()
        )
        assertThat(species.grow(initialPopulation).populationSize)
            .isGreaterThan(initialPopulation.populationSize)
    }

    @Test
    fun die() {
        val initialPopulation = Population(1.0)
        val species = defaultSpecies()
        species.consume(
            initialPopulation, emptyResources()
        )
        assertThat(species.grow(initialPopulation).populationSize)
            .isLessThan(initialPopulation.populationSize)
    }
}
