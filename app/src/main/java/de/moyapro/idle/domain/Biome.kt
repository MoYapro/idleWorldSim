package de.moyapro.idle.domain

import java.text.DecimalFormat
import kotlin.math.*

data class Biome(
    val name: String = "DefaultBiome",
    var resources: Resources = Resources(),
    private val speciesList: MutableCollection<Species> = mutableListOf()
) {
    fun process(): Biome {
        speciesList.shuffled().forEach {
            this.resources = it.process(this.resources)
        }
        return this
    }

    fun settle(species: Species): Biome {
        this.speciesList.add(species)
        this.resources.setPopulation(species, 1.0)
        return this
    }

    fun getStatusText(): String {
        val sb = StringBuilder("BiomeStatus: $name")
        sb.append("\n")
        sb.append(resources)
        speciesList.forEach {
            sb.append("\n")
            sb.append(getStatusText(it))
        }
        return sb.toString()
    }

    private fun getStatusText(species: Species): String {
        return species.name + ": " + (species.getPopulationIn(this)*1E6).toShortDecimalStr() +
                " -> " + (species.process(this.resources).getPopulation(species)*1E6).toShortDecimalStr()
    }
}

fun Double.toShortDecimalStr() : String {
    if (this == 0.0) return "0.0"
    val base = floor(log10(abs(this)) / 3)
    val outVal = round(this / 10.0.pow(base * 3) * 1E10) / 1E10
    return when {
        base < 1 -> outVal.toString()
        base < 2 -> outVal.toString() + 'K'
        base < 3 -> outVal.toString() + 'M'
        base < 4 -> outVal.toString() + 'B'
        base < 5 -> outVal.toString() + 'T'
        else -> outVal.toString() + ('a' + (base.toInt()-5)/26) + ('a' + ((base.toInt()-5)%26))
    }
}
