package de.moyapro.idle.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TraitTest{

    @Test
    fun createGrowthTrait() {
        assertThat(GrowthTrait()).hasFieldOrPropertyWithValue("level", 1)
    }

    @Test
    fun increasedGrowthTrait(){
        assertThat(
            Species().evolve(GrowthTrait()).grow().individualsInMillons)
            .isGreaterThan(Species().grow().individualsInMillons)
    }

    @Test
    fun energySaver() {
        assertThat(Species().evolve(EnergySaver()).generationAndComsumption().energy).isGreaterThan(Species().generationAndComsumption().energy)
    }

    @Test
    fun waterSaver() {
        assertThat(Species().evolve(WaterSaver()).generationAndComsumption().water).isGreaterThan(Species().generationAndComsumption().water)
    }

    @Test
    fun mineralSaver() {
        assertThat(Species().evolve(MineralSaver()).generationAndComsumption().minerals).isGreaterThan(Species().generationAndComsumption().minerals
        )
    }
}