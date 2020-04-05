package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.Species
import de.moyapro.idle.domain.consumption.Consumption
import de.moyapro.idle.domain.consumption.Resource.*
import de.moyapro.idle.domain.consumption.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class FeatureTest {

    @Test
    fun createFeatureWithoutTraits() {
        assertThat(Feature()).isNotNull
    }

    @Test
    fun createFeatureWithTraits() {
        assertThat(Feature(MineralSaver, ConsumerTrait(Minerals))).isNotNull
    }

    @Test
    fun createFeatureWithListOfTraits() {
        assertThat(Feature(traits = setOf(MineralSaver, ConsumerTrait(Minerals)))).isNotNull
    }


    @Test
    fun featureInfluencesLikeTraits() {
        val needs = Resources()
            .setQuantity(Water, 3.0)
        val supply = Resources()
            .setQuantity(Water, 100.0)
        val usableWater = Feature(ConsumerTrait(Water))
            .influenceConsumption(Consumption(Species("Consumer"), needs, supply))
            .usableSupply[Water]
        assertThat(usableWater).isGreaterThan(0.0)
    }


    @Test
    fun emptyFeaturesAreEqualTest() {
        assertThat(Feature()).isEqualTo(Feature())
    }

    @Suppress("SENSELESS_COMPARISON")
    @Test
    fun nullDoesNotEqualAnyFeatures() {
        assertThat(Feature() == null).isFalse()
    }

    @Test
    fun featuresWithTheSameTraitAreEqual() {
        val trait = EnergySaver
        assertThat(Feature(trait) == Feature(trait)).isTrue()
        assertThat(Feature(trait).hashCode() == Feature(trait).hashCode()).isTrue()
    }

    @Test
    fun featuresWithTheDifferentTraitsAreNotEqual() {
        val trait1 = EnergySaver
        val trait2 = Predator(Species("Sheep"))
        assertThat(Feature(trait1) == Feature(trait2)).isFalse()
        assertThat(Feature(trait1).hashCode() == Feature(trait2).hashCode()).isFalse()
    }

    @Test
    fun differentPredatorsAreNotEqual() {
        val trait1 = Predator(Species("Gras"))
        val trait2 = Predator(Species("Sheep"))
        assertThat(Feature(trait1) == Feature(trait2)).isFalse()
        assertThat(Feature(trait1).hashCode() == Feature(trait2).hashCode()).isFalse()
    }

    @Test
    fun samePredatorsAreEqual() {
        val sheep = Species("Sheep")
        val trait1 = Predator(sheep)
        val trait2 = Predator(sheep)
        assertThat(Feature(trait1) == Feature(trait2)).isTrue()
        assertThat(Feature(trait1).hashCode() == Feature(trait2).hashCode()).isTrue()
    }

    @Test
    fun differentConsumersAreNotEqual() {
        val trait1 = ConsumerTrait(Water)
        val trait2 = ConsumerTrait(Minerals)
        assertThat(Feature(trait1) == Feature(trait2)).isFalse()
        assertThat(Feature(trait1).hashCode() == Feature(trait2).hashCode()).isFalse()
    }

    @Test
    fun sameConsumersAreEqual() {
        val trait1 = ConsumerTrait(Energy)
        val trait2 = ConsumerTrait(Energy)
        assertThat(Feature(trait1) == Feature(trait2)).isTrue()
        assertThat(Feature(trait1).hashCode() == Feature(trait2).hashCode()).isTrue()
    }

    @Test
    fun featuresWithTheSameTraitsAreEqual() {
        val traits = setOf(EnergySaver, MineralSaver, GrowthTrait)
        assertThat(Feature(traits = traits) == Feature(traits = traits)).isTrue()
        assertThat(Feature(traits = traits).hashCode() == Feature(traits = traits).hashCode()).isTrue()
    }

    @Test
    fun featureWithSameNameAreEqual() {
        assertThat(Feature("SomeName") == Feature("SomeName")).isTrue()
    }

    @Test
    fun featureWithDifferentNameAreNotEqual() {
        assertThat(Feature("SomeName") == Feature("SomeOTHERName")).isFalse()
    }

    @Test
    fun featureWithSomeDifferentTraitsAreNotEqual() {
        assertThat(Feature(MineralSaver) == Feature(MineralSaver, EnergySaver)).isFalse()
    }
}
