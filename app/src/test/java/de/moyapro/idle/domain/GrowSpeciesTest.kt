package de.moyapro.idle.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GrowSpeciesTest {

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
}