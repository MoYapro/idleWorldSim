package de.moyapro.idle

import de.moyapro.idle.domain.Biome
import de.moyapro.idle.domain.DefaultSpecies
import de.moyapro.idle.domain.WaterSaver

fun main(args: Array<String>) {
    val biome = Biome()
        .settle(DefaultSpecies("3").evolve(WaterSaver()).evolve(WaterSaver()).evolve(WaterSaver()))
        .settle(DefaultSpecies("2").evolve(WaterSaver()).evolve(WaterSaver()))
        .settle(DefaultSpecies("0"))
        .settle(DefaultSpecies("1").evolve(WaterSaver()))

    for(i in 1..100_000) {
        print(biome.getStatusText())
        biome.process()
        Thread.sleep(10)
    }
}
