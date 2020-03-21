package de.moyapro.idle.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpeciesGrowAndDieTest {

    @Test
    fun grow() {
        assertThat(Species().grow().generationAndComsumption().evolutionPoints).isEqualTo(1.1)
    }

    @Test
    fun growForSomeTime() {
        assertThat(Species().grow(10).generationAndComsumption().evolutionPoints).isEqualTo(2.59374246)
    }

    @Test
    fun die() {
        assertThat(Species().die().generationAndComsumption().evolutionPoints).isEqualTo(.95)
    }

    @Test
    fun dieForSomeTime() {
        assertThat(Species().die(10).generationAndComsumption().evolutionPoints).isEqualTo(0.59873694)
    }

}