package de.moyapro.idle.domain

import de.moyapro.idle.domain.consumption.Resource.*
import de.moyapro.idle.domain.consumption.Resources
import de.moyapro.idle.domain.traits.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TraitTest {

    @Test
    fun createGrowthTrait() {
        assertThat(GrowthTrait).hasFieldOrPropertyWithValue("level", 1)
    }

    @Test
    fun increasedGrowthTrait() {
        val species = defaultSpecies()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(
            species.process(resources).getPopulation(species)
        )
            .isLessThan(species.evolve(GrowthTrait).process(resources).getPopulation(species))
    }

    @Test
    fun energySaver() {
        val species = defaultSpecies()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(species.process(resources)[Energy]).isLessThan(species.evolve(EnergySaver).process(resources)[Energy])
    }

    @Test
    fun waterSaver() {
        val species = defaultSpecies()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(species.process(resources)[Water]).isLessThan(species.evolve(WaterSaver).process(resources)[Water])
    }

    @Test
    fun mineralSaver() {
        val species = defaultSpecies()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(species.process(resources)[Minerals]).isLessThan(
            species.evolve(MineralSaver).process(resources)[Minerals]
        )
    }

    @Test
    fun predatorsNeedWater() {
        val sheep = defaultSpecies("sheep")
        val wolf = Species("Wolf").evolve(Predator(sheep), ConsumerTrait(Water))
        assertThat(
            Biome().settle(wolf).settle(sheep)
                .process().resources[wolf]
        ).`as`("Wolf needs water")
            .isGreaterThan(1.0)
    }

    @Test
    fun predatorsCanOnlyEatSomeSpecies() {
        val gras = defaultSpecies("Gras").evolve(ConsumerTrait(Water)).evolve(ConsumerTrait(Minerals)).evolve(
            ConsumerTrait(Energy)
        )
        val sheep = defaultSpecies("Sheep")
        val wolf = Species("Wolf").evolve(Predator(sheep))
        val biome = Biome()
            .settle(gras)
            .settle(wolf)
            .process()

        assertThat(biome.resources.getPopulation(gras)).`as`("Gras not eaten by wolf").isGreaterThan(1.0)
        assertThat(biome.resources.getPopulation(wolf)).`as`("Wolf cannot eat anything").isLessThan(1.0)
    }

    @Test
    fun foldTest() {
        val traits = listOf<Trait>()
        val value = 1
        assertThat(traits.fold(value) { v, _ -> v }).isEqualTo(value)
    }
}
