package de.moyapro.idleworldsim.domain.traits


import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
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
        assertThat(
            Feature(
                MineralSaver,
                ConsumerTrait(Minerals)
            )
        ).isNotNull
    }

    @Test
    fun createFeatureWithListOfTraits() {
        assertThat(
            Feature(
                traits = setOf(
                    MineralSaver,
                    ConsumerTrait(Minerals)
                )
            )
        ).isNotNull
    }

    @Test
    fun hasTraitMeaty() {
        assertThat(Feature(Meaty()).hasTrait(Meaty())).isTrue()
    }

    @Test
    fun hasTraitConsumer() {
        assertThat(Feature(ConsumerTrait(Water)).hasTrait(ConsumerTrait(Water))).isTrue()
    }

    @Test
    fun hasTraitNeed() {
        assertThat(Feature(NeedResource(Water)).hasTrait(NeedResource(Water))).isTrue()
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
        val trait2 = Predator(Meaty())
        assertThat(Feature(trait1) == Feature(trait2)).isFalse()
        assertThat(Feature(trait1).hashCode() == Feature(trait2).hashCode()).isFalse()
    }

    @Test
    fun differentPredatorsAreNotEqual() {
        val trait1 = Predator(Meaty())
        val trait2 = Predator(GrowthTrait)
        assertThat(Feature(trait1) == Feature(trait2)).isFalse()
        assertThat(Feature(trait1).hashCode() == Feature(trait2).hashCode()).isFalse()
    }

    @Test
    fun samePredatorsAreEqual() {
        val trait1 = Predator(Meaty())
        val trait2 = Predator(Meaty())
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
        val traits = setOf(
            EnergySaver,
            MineralSaver,
            GrowthTrait
        )
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
}
