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
        assertThat(species.process(resources).energy).isLessThan(species.evolve(EnergySaver()).process(resources).energy)
    }

    @Test
    fun waterSaver() {
        val species = Species()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(species.process(resources).water).isLessThan(species.evolve(WaterSaver()).process(resources).water)
    }

    @Test
    fun mineralSaver() {
        val species = Species()
        val resources = Resources()
        resources.setPopulation(species, 1.0)
        assertThat(species.process(resources).minerals).isLessThan(species.evolve(MineralSaver()).process(resources).minerals
        )
    }
}
