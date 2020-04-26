package de.moyapro.idleworldsim.util

import de.moyapro.idleworldsim.domain.valueObjects.Population
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun Population.toShortDecimalStr(displayFactor: Double = 1.0, roundFactor: Int = 100): String = value.toShortDecimalStr(displayFactor, roundFactor)

fun Double.toShortDecimalStr(displayFactor: Double = 1.0, roundFactor: Int = 100): String {
    if (this == 0.0) return "0.0"
    val displayValue = this * displayFactor
    val base = floor(log10(abs(displayValue)) / 3)
    val outVal = floor(displayValue / 10.0.pow(base * 3) * roundFactor) / roundFactor
    return when {
        base < 1 -> outVal.toString()
        base < 2 -> outVal.toString() + 'K'
        base < 3 -> outVal.toString() + 'M'
        base < 4 -> outVal.toString() + 'B'
        base < 5 -> outVal.toString() + 'T'
        else -> outVal.toString() + ('a' + (base.toInt() - 5) / 26) + ('a' + ((base.toInt() - 5) % 26))
    }
}
