package de.moyapro.idleworldsim.domain.valueObjects

import de.moyapro.idleworldsim.domain.consumption.DummyPorC
import de.moyapro.idleworldsim.domain.two.Species
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PopulationTest {

    @Test
    fun addEmptyPopulationMaps() {
        assertThat(
            addPopulationMaps(
                emptyMap(),
                emptyMap()
            )
        ).isEqualTo(emptyMap<Species, Population>())
    }

    @Test
    fun addPopulationMaps_WithOnePopEach_OfSameSpecies() {
        val species = DummyPorC("the test species")
        assertThat(
            addPopulationMaps(
                mapOf(Pair(species, Population(1.0))),
                mapOf(Pair(species, Population(2.1)))
            )
        ).isEqualTo(mapOf(Pair(species, Population(3.1))))
    }

    @Test
    fun addPopulationMaps_WithMultiplePopEach_SomeOfSameSpecies() {
        val species1 = DummyPorC("one")
        val species2 = DummyPorC("two")
        val species3 = DummyPorC("three")
        val map1: Map<Species, Population> = mapOf(
            Pair(species1, Population(1.0)),
            Pair(species2, Population(1.0))
        )
        val map2: Map<Species, Population> = mapOf(
            Pair(species1, Population(2.1)),
            Pair(species3, Population(2.1))
        )
        assertThat(
            addPopulationMaps(
                map1,
                map2
            )
        ).isEqualTo(
            mapOf(
                Pair(species1, Population(3.1)),
                Pair(species2, Population(1.0)),
                Pair(species3, Population(2.1))
            )
        )
    }

}