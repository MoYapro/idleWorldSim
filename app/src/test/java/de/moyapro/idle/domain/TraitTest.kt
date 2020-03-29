package de.moyapro.idle.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TraitTest{

    @Test
    fun createGrowthTrait() {
        assertThat(GrowthTrait()).hasFieldOrPropertyWithValue("level", 1)
    }

    @Test
    fun increasedGrowthTrait() {
        val species = Species()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(
            species.process(resources).getPopulation(species))
            .isLessThan(species.evolve(GrowthTrait()).process(resources).getPopulation(species))
    }

    @Test
    fun energySaver() {
        val species = Species()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(species.process(resources)[Resource.Energy]).isLessThan(species.evolve(EnergySaver()).process(resources)[Resource.Energy])
    }

    @Test
    fun waterSaver() {
        val species = Species()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(species.process(resources)[Resource.Water]).isLessThan(species.evolve(WaterSaver()).process(resources)[Resource.Water])
    }

    @Test
    fun mineralSaver() {
        val species = Species()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(species.process(resources)[Resource.Minerals]).isLessThan(species.evolve(MineralSaver()).process(resources)[Resource.Minerals]
        )
    }
}
