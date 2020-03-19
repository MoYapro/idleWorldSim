package de.moyapro.idle.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BiomeTest {
    @Test
    fun createBiome() {
        assertNotNull(Biome(), "Should create biome instance")
    }
    @Test
    fun defaultBiomeIsStable() {
        assertEquals(Biome(), Biome().generate())
    }


}