package de.moyapro.idleworldsim.domain.util

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.util.minus
import de.moyapro.idleworldsim.util.plus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ListExtensionsTest {


    @Test
    fun addPopulations() {
        val species1 = Species("one")
        val species2 = Species("two")
        val species3 = Species("three")
        val list1 = mapOf(
            species1 to Population(1),
            species2 to Population(2)
        )
        val list2 = mapOf(
            species1 to Population(1),
            species3 to Population(3)
        )
        val expectedSum = mapOf(
            species1 to Population(2),
            species2 to Population(2),
            species3 to Population(3)
        )
        assertThat(list1 + list2).isEqualTo(expectedSum)
        assertThat(list2 + list1).isEqualTo(expectedSum)
    }

    @Test
    fun subtractPopulations() {
        val species1 = Species("one")
        val species2 = Species("two")
        val list1 = mapOf(
            species1 to Population(1),
            species2 to Population(2)
        )
        val list2 = mapOf(
            species1 to Population(1),
            species2 to Population(1)
        )
        val expectedSum = mapOf(
            species2 to Population(1)
        )
        assertThat(list1 - list2).isEqualTo(expectedSum)
    }

    @Test
    fun subtractPopulations_error_notInList() {
        val species1 = Species("one")
        val species2 = Species("two")
        val list1 = mapOf(species1 to Population(1))
        val list2 = mapOf(species2 to Population(1))
        assertThatThrownBy { list1 - list2 }.isInstanceOf(IllegalArgumentException::class.java)
    }

}
