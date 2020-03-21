package de.moyapro.idle

import de.moyapro.idle.domain.Biome
import de.moyapro.idle.domain.Species
import de.moyapro.idle.domain.Trait

fun main(args: Array<String>) {
    val biome = Biome()
        .settle(Species().evolve(Trait()).evolve(Trait()).evolve(Trait()))
        .settle(Species().evolve(Trait()).evolve(Trait()))
        .settle(Species())
        .settle(Species().evolve(Trait()))

    for(i in 1..100_000) {
        biome.generate()
        print(biome.getStatusText())
        Thread.sleep(10)
    }
}