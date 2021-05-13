package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.NeedResource
import de.moyapro.idleworldsim.domain.traits.ProduceResource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScenarioTest {

    @Test
    fun biomesHaveAGrowthLimit() {
        val species = Species("4ever_alone")
            .evolveTo(
                Feature(
                    "Eater",
                    ConsumerTrait(Water),
                    NeedResource(Water)
                )
            )
        val biomeWithSpecies =
            Biome().settle(species)
                .addResourceProducer(BiomeFeature("x", Feature("rain", ProduceResource(Water))))
        repeat(1000) {
            biomeWithSpecies.process()
            println(biomeWithSpecies[species])
            print(biomeWithSpecies.lastChanges[species])

        }
        val limit: Double = biomeWithSpecies[species].populationSize
        repeat(1000) {
            biomeWithSpecies.process()
        }
        assertThat(biomeWithSpecies[species].populationSize).isEqualTo(limit)
        assertThat(limit).isGreaterThan(0.0)
    }
}
