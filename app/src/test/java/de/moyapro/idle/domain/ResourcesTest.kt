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

}