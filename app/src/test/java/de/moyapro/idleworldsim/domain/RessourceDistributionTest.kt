package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("MapGetWithNotNullAssertionOperator")
class RessourceDistributionTest {


    @Test
    fun calculateNeeds() {
        val species1 = Species("one").evolve(ConsumerTrait(Water))
        val species2 = Species("two").evolve(ConsumerTrait(Water))
        val species3 = Species("three")
        val available = Resource(Water, 10.0)
        val biome = Biome(resources = Resources(listOf(available)))
            .settle(species1, Population(5.0))
            .settle(species2, Population(3.0))
            .settle(species3, Population(18.0))
        val needs = biome.calculateNeeds()
        assertThat(needs[species1]!!.resource.amount).isEqualTo(5.0)
        assertThat(needs[species2]!!.resource.amount).isEqualTo(3.0)
        assertThat(needs[species3]!!.resource.amount).isEqualTo(0.0)
    }

}
