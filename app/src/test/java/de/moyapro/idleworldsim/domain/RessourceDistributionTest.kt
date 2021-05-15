package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.NeedResource
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Minerals
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.junit.jupiter.api.Test

internal class RessourceDistributionTest {


    @Test
    fun calculateAvailableResources() {
        val species1 = Species("one").evolveTo(Feature(ConsumerTrait(Water), NeedResource(Water)))
        val species2 = Species("two").evolveTo(Feature(ConsumerTrait(Water), NeedResource(Water)))
        val species3 = Species("three").evolveTo(Feature(ConsumerTrait(Minerals), NeedResource(Minerals)))
        val available = Resource(Water, 10.0)
        val biome = Biome()
            .settle(species1, Population(5.0))
            .settle(species2, Population(3.0))
            .settle(species3, Population(18.0))
//        val availableResources = biome.
//        assertThat(availableResources[species1]?.get(Water)?.amount ?: 0.0).isEqualTo(5.0)
//        assertThat(availableResources[species2]?.get(Water)?.amount ?: 0.0).isEqualTo(3.0)
//        assertThat(availableResources[species3]?.get(Water)?.amount ?: 0.0).isEqualTo(0.0) // get no water if not need water
    }

    @Test
    fun calculateAvailableResourcesEqualShare() {
        val species1 = Species("one").evolveTo(Feature(ConsumerTrait(Water), NeedResource(Water)))
        val species2 = Species("two").evolveTo(Feature(ConsumerTrait(Water), NeedResource(Water)))
        val available = Resources(listOf(Resource(Water, 10.0)))
        val biome = Biome()
            .settle(species2, Population(15.0))
            .settle(species1, Population(10.0))
//        val distributedResources = biome.getAquiredResourcesPerSpecies()
//        assertThat(distributedResources.values.sumUsing(Resources::plus)?.getQuantities()?.all { used -> used.amount < available[used.resourceType].amount })
//            .`as`("Should not ave distributed more that is availble")
//        assertThat(distributedResources[species1]?.get(Water)?.amount ?: 0.0).isEqualTo(5.0)
//        assertThat(distributedResources[species2]?.get(Water)?.amount ?: 0.0).isEqualTo(5.0)
    }

}
