package de.moyapro.idle.domain

import de.moyapro.idleworldsim.domain.consumption.Resource.*
import de.moyapro.idleworldsim.domain.consumption.ResourceFactor
import de.moyapro.idleworldsim.domain.consumption.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class ResourcesTest {
    @Test
    fun resourceCanProvideAll() {
        assertThat(Resources().canProvide(Resources()).all { it }).isTrue()
    }

    @Test
    fun cannotProvideWater() {
        assertThat(
            Resources()
                .setQuantity(Water, 1.0).canProvide(Resources().setQuantity(Water, 2.0))
                .all { it }
        ).isFalse()
    }


    @Test
    fun cannotProvideEnergy() {
        assertThat(
            Resources()
                .setQuantity(Energy, 1.0).canProvide(Resources().setQuantity(Energy, 2.0))
                .all { it }
        ).isFalse()
    }

    @Test
    fun cannotProvideMinerals() {
        assertThat(
            Resources()
                .setQuantity(Minerals, 1.0).canProvide(Resources().setQuantity(Minerals, 2.0))
                .all { it }
        ).isFalse()
    }

    @Test
    fun canProvideWater() {
        assertThat(
            Resources()
                .setQuantity(Water, 10.0).canProvide(Resources().setQuantity(Water, 2.0))
                .all { it }
        ).isTrue()
    }

    @Test
    fun canProvideMinerals() {
        assertThat(
            Resources()
                .setQuantity(Minerals, 10.0).canProvide(Resources().setQuantity(Minerals, 2.0))
                .all { it }
        ).isTrue()
    }

    @Test
    fun canProvideEnergy() {
        assertThat(
            Resources(Energy, 10.0)
                .canProvide(Resources().setQuantity(Energy, 2.0))
        .all { it }
        )
            .isTrue()
    }

    @Test
    fun plus() {
        assertThat(
            Resources(doubleArrayOf(1.0, 1.0, 1.0, 1.0))
                    + Resources(doubleArrayOf(2.1, 2.0, 2.0, 2.0))
        ).isEqualTo(
            Resources(doubleArrayOf(3.1, 3.0, 3.0, 3.0))
        )
    }

    @Test
    fun minus() {
        assertThat(
            Resources(doubleArrayOf(2.1, 2.0, 2.0, 2.0))
                    + Resources(doubleArrayOf(-1.0, -1.0, -1.0, -1.0))
        ).isEqualTo(
            Resources(doubleArrayOf(1.1, 1.0, 1.0, 1.0))
        )
    }

    @Test
    fun timesScalar() {
        assertThat(Resources(doubleArrayOf(5.0, 5.0, 5.0, 5.0)) * 3.0)
            .isEqualTo(Resources(doubleArrayOf(15.0, 15.0, 15.0, 15.0)))
    }

    @Test
    fun timesResourceFactors() {
        assertThat(
            Resources(
                doubleArrayOf(
                    5.0,
                    5.0,
                    5.0,
                    5.0
                )
            ) * ResourceFactor(3.0, 3.0, 3.0, 3.0)
        )
            .isEqualTo(Resources(doubleArrayOf(15.0, 15.0, 15.0, 15.0)))
    }

    @Test
    fun createResourcesWithMap() {
        assertThat(
            Resources(
                mapOf(Pair(Energy, 0.0))
            )
        ).isNotNull
    }

    @Test
    fun setQuantity() {
        val theValue = 666.3
        assertThat(Resources().setQuantity(Water, theValue)[Water]).isEqualTo(theValue)
    }

    @Test
    fun setDefaultQuantity() {
        assertThat(Resources().setQuantity(Water)[Water]).isEqualTo(1.0)
    }

    @Test
    fun setDefaultPolulation() {
        val theSpecies = Species("X")
        assertThat(Resources().setPopulation(theSpecies)[theSpecies]).isEqualTo(1.0)
    }

    @Test
    fun setPolulation() {
        val theValue = 345.678
        val theSpecies = Species("X")
        assertThat(Resources().setPopulation(theSpecies, theValue)[theSpecies]).isEqualTo(theValue)
    }

    @Test
    fun hashCodeEquals() {
        val resources1 = Resources().setQuantity(Minerals, 13.0)
        val resources2 = Resources().setQuantity(Minerals, 13.0)
        assertThat(resources1).isEqualTo(resources2)
        assertThat(resources1.hashCode()).isEqualTo(resources2.hashCode())
    }

    @Test
    fun hashCodeNotEquals() {
        val resources1 = Resources().setQuantity(Minerals, 99.0)
        val resources2 = Resources().setQuantity(Water, 2.0)
        assertThat(resources1).isNotEqualTo(resources2)
        assertThat(resources1.hashCode()).isNotEqualTo(resources2.hashCode())
    }

}
