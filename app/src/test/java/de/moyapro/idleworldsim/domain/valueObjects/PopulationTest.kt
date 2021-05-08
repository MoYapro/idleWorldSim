package de.moyapro.idleworldsim.domain.valueObjects

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.TraitBearer
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
        val species = Species("the test species")
        assertThat(
            addPopulationMaps(
                mapOf(Pair(species, Population(1.0))),
                mapOf(Pair(species, Population(2.1)))
            )
        ).isEqualTo(mapOf(Pair(species, Population(3.1))))
    }

    @Test
    fun addPopulationMaps_WithMultiplePopEach_SomeOfSameSpecies() {
        val species1 = Species("one")
        val species2 = Species("two")
        val species3 = Species("three")
        val map1: Map<TraitBearer, Population> = mapOf(
            Pair(species1, Population(1.0)),
            Pair(species2, Population(1.0))
        )
        val map2: Map<TraitBearer, Population> = mapOf(
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

    @Test
    fun population_isNotEmpty() {
        assertThat(Population(1).isNotEmpty()).isTrue()
        assertThat(Population(0).isNotEmpty()).isFalse()
    }

    @Test
    fun population_isEmpty() {
        assertThat(Population(1).isEmpty()).isFalse()
        assertThat(Population(0).isEmpty()).isTrue()
    }


}