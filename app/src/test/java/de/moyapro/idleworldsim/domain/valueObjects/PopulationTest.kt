package de.moyapro.idleworldsim.domain.valueObjects

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.TraitBearer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class PopulationTest {

    @Test
    fun addEmptyPopulationMaps() {
        assertThat(
            emptyMap<Species, Population>().applyChanges(emptyMap())
        ).isEqualTo(emptyMap<Species, Population>())
    }

    @Test
    fun addPopulationMaps_WithOnePopEach_OfSameSpecies() {
        val species = Species("the test species")
        val speciesMap = mapOf(Pair(species, Population(1.0)))
        val changes = mapOf(Pair(species, PopulationChange(2.1)))
        assertThat(
            speciesMap.applyChanges(changes)
        ).isEqualTo(mapOf(Pair(species, Population(3.1))))
    }

    @Test
    fun addPopulationMaps_WithMultiplePopEach_SomeOfSameSpecies() {
        val species1 = Species("one")
        val species2 = Species("two")
        val species3 = Species("three")
        val speciesMap: Map<TraitBearer, Population> = mapOf(
            Pair(species1, Population(1.0)),
            Pair(species2, Population(1.0))
        )
        val changes: Map<TraitBearer, PopulationChange> = mapOf(
            Pair(species1, PopulationChange(2.1)),
            Pair(species3, PopulationChange(2.1))
        )
        assertThat(
            speciesMap.applyChanges(changes)
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

    @Test
    fun plusPopulationChange_positive() {
        assertThat((Population(1) + PopulationChange(1)).populationSize).isEqualTo(2.0)
    }

    @Test
    fun plusPopulationChange_negative() {
        assertThat((Population(2) + PopulationChange(-1)).populationSize).isEqualTo(1.0)
    }

    @Test
    fun plusPopulationChange_negativeResultThrowsException() {
        assertThatThrownBy { Population(2) + PopulationChange(-99999) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun addPopulations() {
        val species1 = Species("one")
        val species2 = Species("two")
        val species3 = Species("three")
        val map1 = mapOf(
            species1 to Population(1),
            species2 to Population(2)
        )
        val map2 = mapOf(
            species1 to PopulationChange(4),
            species3 to PopulationChange(3)
        )
        val expectedSum = mapOf(
            species1 to Population(5),
            species2 to Population(2),
            species3 to Population(3)
        )
        assertThat(map1.applyChanges(map2)).isEqualTo(expectedSum)
    }

    @Test
    fun subtractPopulations() {
        val species1 = Species("one")
        val species2 = Species("two")
        val speciesMap = mapOf(
            species1 to Population(1),
            species2 to Population(2)
        )
        val changesMap = mapOf(
            species1 to PopulationChange(-1),
            species2 to PopulationChange(-1)
        )
        val expectedSum = mapOf(
            species2 to Population(1)
        )
        assertThat(speciesMap.applyChanges(changesMap)).isEqualTo(expectedSum)
    }

    @Test
    fun subtractPopulations_error_notInList() {
        val species1 = Species("one")
        val species2 = Species("two")
        val list1 = mapOf(species1 to Population(1))
        val list2 = mapOf(species2 to PopulationChange(-1))
        assertThatThrownBy { list1.applyChanges(list2) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun combinePopulationChangeMaps_add_same() {
        val species1 = Species("one")
        val map1 = mapOf(species1 to PopulationChange(3))
        assertThat(map1.combineWith(map1)).isEqualTo(mapOf(species1 to PopulationChange(6)))
    }

    @Test
    fun combinePopulationChangeMaps_add_different() {
        val species1 = Species("one")
        val species2 = Species("two")
        val map1 = mapOf(species1 to PopulationChange(3))
        val map2 = mapOf(species2 to PopulationChange(5))

        assertThat(map1.combineWith(map2)).isEqualTo(
            mapOf(
                species1 to PopulationChange(3),
                species2 to PopulationChange(5)
            )
        )
    }

    @Test
    fun combinePopulationChangeMaps_negative_same() {
        val species1 = Species("one")
        val map1 = mapOf(species1 to PopulationChange(-3))
        assertThat(map1.combineWith(map1)).isEqualTo(mapOf(species1 to PopulationChange(-6)))
    }

    @Test
    fun combinePopulationChangeMaps_subtract_different() {
        val species1 = Species("one")
        val species2 = Species("two")
        val map1 = mapOf(species1 to PopulationChange(3))
        val map2 = mapOf(species2 to PopulationChange(-1))
        assertThat(map1.combineWith(map2)).isEqualTo(
            mapOf(
                species1 to PopulationChange(3),
                species2 to PopulationChange(-1)
            )
        )


        @Test
        fun isUnchanged() {
            assertThat(PopulationChange.NO_CHANGE.isUnchanged()).isTrue()
            assertThat(PopulationChange(0).isUnchanged()).isTrue()
            assertThat(PopulationChange(1).isUnchanged()).isFalse()
        }

        @Test
        fun removeUnchangedFromMap_allUnchanged() {
            val changesMap = mapOf(1 to PopulationChange.NO_CHANGE)
            assertThat(changesMap.removeUnchanged()).isEmpty()
        }

        @Test
        fun removeUnchangedFromMap_allChanged() {
            val changesMap = mapOf(1 to PopulationChange(1))
            assertThat(changesMap.removeUnchanged()).isNotEmpty()
        }

        @Test
        fun removeUnchangedFromMap_mixed() {
            val changesMap = mapOf(
                1 to PopulationChange(1),
                2 to PopulationChange.NO_CHANGE
            )
            assertThat(changesMap.removeUnchanged()).hasSize(1)
        }


    }


}
