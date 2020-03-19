package de.moyapro.idle.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ResourceConsumptionAndGenerationTest {
    @Test
    fun generateDefault() {
        assertEquals(1.0, Species().generate().amount, "Should generate in default settings")
    }

    @Test
    fun generateForSomeTime() {
        assertEquals(2.0, Species().generate(2).amount, "Should generate for multiple seconds")
    }

    @Test
    fun generateSpeciesWithTraits() {
        val species:Species = Species().evolve(Trait()).evolve(Trait())
        assertEquals(1.3225, species.generate().amount, .000001, "Should calculate generation including trait")
    }
}