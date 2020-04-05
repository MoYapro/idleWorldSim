package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Resource
import de.moyapro.idle.domain.consumption.Resources
import de.moyapro.idle.domain.traits.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
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
        val expectedTratis = setOf(MineralSaver, ConsumerTrait(Resource.Minerals))
        assertThat(
            Species("Species2", mutableSetOf(Feature("Excludes GrowthTrait", traits1, excludedTraits), Feature(traits = traits2))).getEffectiveTraits()
        )
            .isEqualTo(expectedTratis)
    }

    @Test
    fun excludedTraitNotActiveInSpecies() {
        val speciesWithGrothImprovemnt = defaultSpecies().evolve(Feature(GrowthTrait))
        val speciesWithExcludedGrothImprovemnt = defaultSpecies().evolve(Feature(GrowthTrait), Feature(excludedTraits = mutableSetOf(GrowthTrait)))
        val speciesWithoutGrothImprovemnt = defaultSpecies()

        val totalSupplyFromBiome = Resources(populations = mutableMapOf(Pair(speciesWithGrothImprovemnt, 1.0), Pair(speciesWithExcludedGrothImprovemnt, 1.0), Pair(speciesWithoutGrothImprovemnt, 1.0)))
        val speciesWithGrothImprovemntPopulation = speciesWithGrothImprovemnt.process(totalSupplyFromBiome)[speciesWithGrothImprovemnt]
        val speciesWithExcludedGrothImprovemntPopulation = speciesWithExcludedGrothImprovemnt.process(totalSupplyFromBiome)[speciesWithExcludedGrothImprovemnt]
        val speciesWithoutGrothImprovemntPopulation = speciesWithoutGrothImprovemnt.process(totalSupplyFromBiome)[speciesWithoutGrothImprovemnt]

        assertThat(speciesWithGrothImprovemntPopulation).isEqualTo(1.21, Offset.offset(0.00001))
        assertThat(speciesWithoutGrothImprovemntPopulation).isEqualTo(1.1)
        assertThat(speciesWithExcludedGrothImprovemntPopulation).isEqualTo(1.1)
    }

}
