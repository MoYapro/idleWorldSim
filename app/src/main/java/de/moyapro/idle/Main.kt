package de.moyapro.idle

import de.moyapro.idle.domain.Biome
import de.moyapro.idle.domain.Species
import de.moyapro.idle.domain.Trait
import de.moyapro.idle.domain.WaterSaver

fun main(args: Array<String>) {
    val biome = Biome()
        .settle(Species().evolve(WaterSaver()).evolve(WaterSaver()).evolve(WaterSaver()))
        .settle(Species().evolve(WaterSaver()).evolve(WaterSaver()))
        .settle(Species())
        .settle(Species().evolve(WaterSaver()))

    for(i in 1..100_000) {
        biome.generate()
        print(biome.getStatusText())
        Thread.sleep(10)
    }
}