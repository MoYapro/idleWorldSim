package de.moyapro.idle

import de.moyapro.idle.domain.Biome
import de.moyapro.idle.domain.Species
import de.moyapro.idle.domain.WaterSaver

fun main(args: Array<String>) {
    val biome = Biome()
        .settle(Species("3").evolve(WaterSaver()).evolve(WaterSaver()).evolve(WaterSaver()))
        .settle(Species("2").evolve(WaterSaver()).evolve(WaterSaver()))
        .settle(Species("0"))
        .settle(Species("1").evolve(WaterSaver()))

    for(i in 1..100_000) {
        print(biome.getStatusText())
        biome.process()
        Thread.sleep(10)
    }
}
