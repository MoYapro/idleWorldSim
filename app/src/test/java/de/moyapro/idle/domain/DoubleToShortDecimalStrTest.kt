package de.moyapro.idle.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DoubleToShortDecimalStrTest {
    @Test
    fun toShortDecimalStr0() {
        assertThat(0.0.toShortDecimalStr()).isEqualTo("0.0")
    }

    @Test
    fun toShortDecimalStrN1K() {
        assertThat((-1000.0).toShortDecimalStr()).isEqualTo("-1.0K")
    }

    @Test
    fun toShortDecimalStrLess1K() {
        assertThat(999.9999.toShortDecimalStr()).isEqualTo("999.9999")
    }

    @Test
    fun toShortDecimalStr1K() {
        assertThat(1000.0.toShortDecimalStr()).isEqualTo("1.0K")
    }
    @Test
    fun toShortDecimalStr1_7K() {
        assertThat(1716.4.toShortDecimalStr()).isEqualTo("1.7K")
    }
    @Test
    fun toShortDecimalStr1M() {
        assertThat(1E6.toShortDecimalStr()).isEqualTo("1.0M")
    }

    @Test
    fun toShortDecimalStr1B() {
        assertThat(1E9.toShortDecimalStr()).isEqualTo("1.0B")
    }

    @Test
    fun toShortDecimalStr1T() {
        assertThat(1E12.toShortDecimalStr()).isEqualTo("1.0T")
    }

    @Test
    fun toShortDecimalStr1aa() {
        assertThat(1E15.toShortDecimalStr()).isEqualTo("1.0aa")
    }

    @Test
    fun toShortDecimalStr1ab() {
        assertThat(1E18.toShortDecimalStr()).isEqualTo("1.0ab")
    }
}
