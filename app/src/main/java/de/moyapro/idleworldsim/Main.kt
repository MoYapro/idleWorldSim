package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.defaultSpecies
import de.moyapro.idleworldsim.domain.evolveTo
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.WaterSaver
import de.moyapro.idleworldsim.util.QuickAndDirty

@QuickAndDirty
object GameX {
    val biome = Biome()


    fun process() {
        biome.process()
    }
}

fun main(args: Array<String>) {
    init()

    for (i in 1..100_000) {
        print(Game.getStatusText())
        Game.process()
        Thread.sleep(10)
    }
}

fun init() {
    GameX.biome
        .settle(
            defaultSpecies("3")
                .evolveTo(Feature(WaterSaver))
                .evolveTo(Feature(WaterSaver))
                .evolveTo(Feature(WaterSaver))
        )
        .settle(
            defaultSpecies("2")
                .evolveTo(Feature(WaterSaver))
                .evolveTo(Feature(WaterSaver))
        )
        .settle(defaultSpecies("0"))
        .settle(defaultSpecies("1").evolveTo(Feature(WaterSaver)))

}
