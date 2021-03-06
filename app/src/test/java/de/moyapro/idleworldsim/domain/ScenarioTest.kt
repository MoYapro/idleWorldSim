package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.NeedResource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
import org.junit.jupiter.api.Test

class ScenarioTest {

    @Test
    fun biomesHaveAGrowthLimit() {
        val biomeWithSpecies =
            Biome()
                .settle(
                    Species("4ever_alone")
                        .evolveTo(Feature("Eater", ConsumerTrait(ResourceType.Water), NeedResource(ResourceType.Water)))
                )
    }
}