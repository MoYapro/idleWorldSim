package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.traits.NeedResource
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Minerals
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("MapGetWithNotNullAssertionOperator")
class RessourceDistributionTest {


    @Test
    fun calculateAvailableResources() {
        val species1 = Species("one").evolve(ConsumerTrait(Water), NeedResource(Water))
        val species2 = Species("two").evolve(ConsumerTrait(Water), NeedResource(Water))
        val species3 = Species("three").evolve(ConsumerTrait(Minerals), NeedResource(Minerals))
        val available = Resource(Water, 10.0)
        val biome = Biome(resources = Resources(listOf(available)))
            .settle(species1, Population(5.0))
            .settle(species2, Population(3.0))
            .settle(species3, Population(18.0))
        val availableResources = biome.getAvailableResourcesPerSpecies()
        assertThat(availableResources[species1]?.get(Water)?.amount ?: 0.0).isEqualTo(5.0)
        assertThat(availableResources[species2]?.get(Water)?.amount ?: 0.0).isEqualTo(3.0)
        assertThat(availableResources[species3]?.get(Water)?.amount ?: 0.0).isEqualTo(0.0)
    }

    @Test
    fun calculateAvailableResourcesEqualShare() {
        val species1 = Species("one").evolve(ConsumerTrait(Water), NeedResource(Water))
        val species2 = Species("two").evolve(ConsumerTrait(Water), NeedResource(Water))
        val available = Resource(Water, 10.0)
        val biome = Biome(resources = Resources(listOf(available)))
            .settle(species1, Population(10.0))
            .settle(species2, Population(15.0))
        val availableResources = biome.getAvailableResourcesPerSpecies()
        assertThat(availableResources[species1]?.get(Water)?.amount ?: 0.0).isEqualTo(5.0)
        assertThat(availableResources[species2]?.get(Water)?.amount ?: 0.0).isEqualTo(5.0)
    }

}
