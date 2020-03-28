package de.moyapro.idle.util

import kotlin.math.*

fun Double.toShortDecimalStr(): String {
    if (this == 0.0) return "0.0"
    val base = floor(log10(abs(this)) / 3)
    val roundFactor = 100
    val outVal = floor(this / 10.0.pow(base * 3) * roundFactor) / roundFactor
    return when {
        base < 1 -> outVal.toString()
        base < 2 -> outVal.toString() + 'K'
        base < 3 -> outVal.toString() + 'M'
        base < 4 -> outVal.toString() + 'B'
        base < 5 -> outVal.toString() + 'T'
        else -> outVal.toString() + ('a' + (base.toInt() - 5) / 26) + ('a' + ((base.toInt() - 5) % 26))
    }
}