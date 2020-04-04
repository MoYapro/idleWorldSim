package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.Species
import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource.Minerals
import de.moyapro.idle.domain.consumption.Resource.Water
import de.moyapro.idle.domain.consumption.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FeatureTest {

    @Test
    fun createFeatureWithoutTraits() {
        assertThat(Feature()).isNotNull
    }

    @Test
    fun createFeatureWithTraits() {
        assertThat(Feature(MineralSaver(), ConsumerTrait(Minerals))).isNotNull
    }

    @Test
    fun createFeatureWithListOfTraits() {
        assertThat(Feature(listOf(MineralSaver(), ConsumerTrait(Minerals)))).isNotNull
    }


    @Test
    fun featureInfluencesLikeTraits() {
        val needs = Resources()
            .setQuantity(Water, 3.0)
        val supply = Resources()
            .setQuantity(Water, 100.0)
        val usableWater = Feature(ConsumerTrait(Water))
            .influence(Consumption(Species("Consumer"), needs, supply))
            .usableSupply[Water]
        assertThat(usableWater).isGreaterThan(0.0)
    }
}
