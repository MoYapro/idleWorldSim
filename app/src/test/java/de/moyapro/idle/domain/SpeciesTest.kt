package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Resource
import de.moyapro.idle.domain.traits.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SpeciesTest {
    @Test
    fun effectiveEmptyTraits() {
        assertThat(
            Species("Species1", Feature()).getEffectiveTraits()
        )
            .isEqualTo(emptySet<Trait>())
    }

    @Test
    fun effectiveOneTrait() {
        val traits = setOf(ConsumerTrait(Resource.Water))
        assertThat(
            Species("Species2", Feature(traits = traits)).getEffectiveTraits()
        )
            .isEqualTo(traits)
    }

    @Test
    fun effectiveMultipleTraits() {
        val traits1 = setOf(ConsumerTrait(Resource.Water), GrowthTrait)
        val traits2 = setOf(GrowthTrait, MineralSaver)
        val expectedTratis = setOf(GrowthTrait, MineralSaver, ConsumerTrait(Resource.Water))
        assertThat(
            Species("Species2", mutableSetOf(Feature(traits = traits1), Feature(traits = traits2))).getEffectiveTraits()
        )
            .isEqualTo(expectedTratis)
    }

    @Test
    fun effectiveExcludingTraits() {
        val traits1 = setOf(ConsumerTrait(Resource.Minerals))
        val traits2 = setOf(GrowthTrait, MineralSaver)
        val excludedTraits = setOf(GrowthTrait)
        val expectedTratis = setOf(MineralSaver, ConsumerTrait(Resource.Water))
        assertThat(
            Species("Species2", mutableSetOf(Feature("Excludes GrowthTrait", traits1, excludedTraits), Feature(traits = traits2))).getEffectiveTraits()
        )
            .isEqualTo(expectedTratis)
    }

}
