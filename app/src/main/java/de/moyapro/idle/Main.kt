package de.moyapro.idle

import de.moyapro.idle.domain.Biome
import de.moyapro.idle.domain.defaultSpecies
import de.moyapro.idle.domain.traits.WaterSaver

fun main(args: Array<String>) {
    val biome = Biome()
        .settle(
            defaultSpecies("3").evolve(WaterSaver()).evolve(WaterSaver()).evolve(
                WaterSaver()
            )
        )
        .settle(defaultSpecies("2").evolve(WaterSaver()).evolve(WaterSaver()))
        .settle(defaultSpecies("0"))
        .settle(defaultSpecies("1").evolve(WaterSaver()))

    for(i in 1..100_000) {
        print(biome.getStatusText())
        biome.process()
        Thread.sleep(10)
    }
}
