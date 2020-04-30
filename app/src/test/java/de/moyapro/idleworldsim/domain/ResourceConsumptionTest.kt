package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceType
import de.moyapro.idleworldsim.domain.consumption.ResourceType.EvolutionPoints
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.EvolutionBooster
import de.moyapro.idleworldsim.domain.traits.ProduceResource
import de.moyapro.idleworldsim.domain.util.defaultOffset
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ResourceConsumptionTest {
    @Test
    fun generateDefault() {
        assertThat(
            defaultSpecies().process(Resources(doubleArrayOf(0.0, 1.0, 1.0, 1.0)))[EvolutionPoints]
        ).isEqualTo(1.0)
    }

    @Test
    fun generateDependingOnNumberOfIndividuals() {
        val species = defaultSpecies()
        val totalSupplyFromBiome = Resources(doubleArrayOf(0.0, 3.0, 3.0, 3.0, 3.0)).setPopulation(species, 2.0)
        assertThat(species.process(totalSupplyFromBiome))
            .isEqualTo(Resources(doubleArrayOf(2.0, 1.0, 1.0, 1.0, 3.0)).setPopulation(species, 2.2))
    }

    @Test
    fun generateSpeciesWithTraits() {
        val species = defaultSpecies()
            .evolve(EvolutionBooster, ProduceResource(EvolutionPoints))
        val availableResources = Resources(DoubleArray(ResourceType.values().size) { if (it == EvolutionPoints.ordinal) 0.0 else 3.0 })
        assertThat(
            species.process(availableResources.setPopulation(species, 1.0))[EvolutionPoints]
        ).isEqualTo(
            1.15,
            defaultOffset()
        )
    }
}
