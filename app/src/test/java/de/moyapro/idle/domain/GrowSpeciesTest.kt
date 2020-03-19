package de.moyapro.idle.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SpeciesGrowAndDieTest {

    @Test
    fun grow() {
        assertEquals(1.1, Species().grow().generate(), "Should generate more after growing")
    }

    @Test
    fun growForSomeTime() {
        assertEquals(
            2.59374246,
            Species().grow(10).generate(),
            0.000001,
            "Should generate more after growing"
        )
    }

    @Test
    fun die() {
        assertEquals(.95, Species().die().generate(), "Should generate more after growing")
    }

    @Test
    fun dieForSomeTime() {
        assertEquals(0.59873694, Species().die(10).generate(), 0.0000001,"Should generate more after growing")
    }

}