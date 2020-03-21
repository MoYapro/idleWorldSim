package de.moyapro.idle.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class ResourcesTest {
    @Test
    fun resourceCanProvideAll() {
        assertThat(Resources().canProvide(Resources())).isTrue()
    }

    @Test
    fun cannotProvideWater() {
        assertThat(Resources(water = 1).canProvide(Resources(water = -2))).isFalse()
    }

    @Test
    fun cannotProvideEnergy() {
        assertThat(Resources(energy = 1).canProvide(Resources(energy = -2))).isFalse()
    }

    @Test
    fun cannotProvideMinerals() {
        assertThat(Resources(minerals = 1).canProvide(Resources(minerals = -2))).isFalse()
    }

    @Test
    fun canProvideWater() {
        assertThat(Resources(water = 10).canProvide(Resources(water = -2))).isTrue()
    }

    @Test
    fun canProvideMinerals() {
        assertThat(Resources(minerals = 10).canProvide(Resources(minerals = -2))).isTrue()
    }

    @Test
    fun canProvideEnergy() {
        assertThat(Resources(energy = 10).canProvide(Resources(energy = -2))).isTrue()
    }

    @Test
    fun plus() {
        assertThat(
            Resources(1.0, 1, 1, 1)
                .plus(Resources(2.1, 2, 2, 2))
        ).isEqualTo(
            Resources(3.1, 3, 3, 3)
        )
    }

    @Test
    fun minus() {
        assertThat(
            Resources(2.1, 2, 2, 2)
                .plus(Resources(-1.0, -1, -1, -1))
        ).isEqualTo(
            Resources(1.1, 1, 1, 1)
        )
    }

    @Test
    fun timesScalar() {
        assertThat(Resources(5.0, 5, 5, 5).times(3.0))
            .isEqualTo(Resources(15.0, 15, 15, 15))
    }

    @Test
    fun timesResourceFactors() {
        assertThat(Resources(5.0, 5, 5, 5).times(ResourceFactor(3.0, 3.0, 3.0, 3.0 )))
            .isEqualTo(Resources(15.0, 15, 15, 15))
    }


}