package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.EvolutionBooster
import de.moyapro.idleworldsim.domain.traits.ProduceResource
import de.moyapro.idleworldsim.domain.util.defaultOffset
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ResourceConsumptionTest {
    @Test
    fun generateDefault() {
        assertThat(
            defaultSpecies().process(Resources(doubleArrayOf(0.0, 1.0, 1.0, 1.0)))[EvolutionPoints].amount
        ).isEqualTo(1.0)
    }

    @Test
    fun generateDependingOnNumberOfIndividuals() {
        val species = defaultSpecies()
        val totalSupplyFromBiome = Resources(
            listOf(
                Resource(Energy, 3.0),
                Resource(Water, 3.0),
                Resource(Minerals, 3.0),
                Resource(Oxygen, 3.0)
            )
        ).setPopulation(species, Population(2.0))

        val expectedResourcesAfterConsumption = Resources(
            listOf(
                Resource(EvolutionPoints, 2.0),
                Resource(Energy, 1.0),
                Resource(Water, 1.0),
                Resource(Minerals, 1.0),
                Resource(Oxygen, 3.0)
            )
        )

        assertThat(species.process(totalSupplyFromBiome))
            .isEqualTo(expectedResourcesAfterConsumption.setPopulation(species, Population(2.2)))
    }

    @Test
    fun generateSpeciesWithTraits() {
        val species = defaultSpecies()
            .evolve(EvolutionBooster, ProduceResource(EvolutionPoints))
        val availableResources = Resources(DoubleArray(values().size) { if (it == EvolutionPoints.ordinal) 0.0 else 3.0 })
        assertThat(
            species.process(availableResources.setPopulation(species, Population(1.0)))[EvolutionPoints].amount
        ).isEqualTo(
            1.15,
            defaultOffset()
        )
    }
}
