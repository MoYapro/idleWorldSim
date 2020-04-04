package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.Species
import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource.Minerals
import de.moyapro.idle.domain.consumption.Resource.Water
import de.moyapro.idle.domain.consumption.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class FeatureTest {

    @Test
    fun createFeatureWithoutTraits() {
        assertThat(Feature(MineralSaver(), ConsumerTrait(Minerals))).isNotNull
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


    @Test
    fun emptyFeaturesAreEqualTest() {
        assertThat(Feature()).isEqualTo(Feature())
    }

    @Test
    fun nullDoesNotEqualAnyFeatures() {
        assertThat(Feature() == null).isFalse()
    }

    @Test
    fun featuresWithTheSameTraitAreEqual() {
        val trait = EnergySaver
        assertThat(Feature(trait) == Feature(trait)).isTrue()
    }

    @Test
    fun featuresWithTheDifferentTraitsAreNotEqual() {
        val trait1 = EnergySaver
        val trait2 = Predator(Species("Sheep"))
        assertThat(Feature(trait1) == Feature(trait2)).isFalse()
    }

    @Test
    fun featuresWithTheSameTraitsAreEqual() {
        val traits = listOf(EnergySaver, MineralSaver(), GrowthTrait)
        assertThat(Feature(traits) == Feature(traits)).isTrue()
    }


}