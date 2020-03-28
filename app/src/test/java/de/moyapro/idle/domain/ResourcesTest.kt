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
        assertThat(Resources(water = 1.0).canProvide(Resources(water = 2.0))).isFalse()
    }

    @Test
    fun cannotProvideEnergy() {
        assertThat(Resources(energy = 1.0).canProvide(Resources(energy = 2.0))).isFalse()
    }

    @Test
    fun cannotProvideMinerals() {
        assertThat(Resources(minerals = 1.0).canProvide(Resources(minerals = 2.0))).isFalse()
    }

    @Test
    fun canProvideWater() {
        assertThat(Resources(water = 10.0).canProvide(Resources(water = 2.0))).isTrue()
    }

    @Test
    fun canProvideMinerals() {
        assertThat(Resources(minerals = 10.0).canProvide(Resources(minerals = 2.0))).isTrue()
    }

    @Test
    fun canProvideEnergy() {
        assertThat(Resources(energy = 10.0).canProvide(Resources(energy = 2.0))).isTrue()
    }

    @Test
    fun plus() {
        assertThat(
            Resources(1.0, 1.0, 1.0, 1.0)
                + Resources(2.1, 2.0, 2.0, 2.0)
        ).isEqualTo(
            Resources(3.1, 3.0, 3.0, 3.0)
        )
    }

    @Test
    fun minus() {
        assertThat(
            Resources(2.1, 2.0, 2.0, 2.0)
                + Resources(-1.0, -1.0, -1.0, -1.0)
        ).isEqualTo(
            Resources(1.1, 1.0, 1.0, 1.0)
        )
    }

    @Test
    fun timesScalar() {
        assertThat(Resources(5.0, 5.0, 5.0, 5.0) * 3.0)
            .isEqualTo(Resources(15.0, 15.0, 15.0, 15.0))
    }

    @Test
    fun timesResourceFactors() {
        assertThat(Resources(5.0, 5.0, 5.0, 5.0) * ResourceFactor(3.0, 3.0, 3.0, 3.0 ))
            .isEqualTo(Resources(15.0, 15.0, 15.0, 15.0))
    }


}
