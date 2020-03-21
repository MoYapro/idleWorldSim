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
}