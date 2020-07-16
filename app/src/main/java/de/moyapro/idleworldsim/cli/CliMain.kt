package de.moyapro.idleworldsim.cli

import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.traits.Feature
import java.util.*
import kotlin.system.exitProcess

fun main() {
    println("Welcome")
    println(Game.help)


    while (true) {
        val split = readLine()!!.split(' ')
        val command = split.first()
        val commandArgument: String? = if (split.size > 1) split[1] else null
        when (command.toUpperCase(Locale.getDefault())) {
            "B" -> executeCommandBiome(commandArgument)
            "S" -> executeSpeciesCommand(commandArgument)
            "E" -> executeEvolveCommand(commandArgument)
            "H" -> println(Game.help)
            "Q" -> exitProcess(0)
        }
    }
}

fun executeEvolveCommand(commandArgument: String?) {
    if (null == commandArgument) {
        println(Game.selectedBiome)
        println(Game.selectedSpecies)
        outputFeatures(Game.getEvolveOptions())
        return
    }
    val featureToSelect = commandArgument.toInt()
    val selectedFeature = Game.getEvolveOptions()[featureToSelect]
    val newSpecies = Game.selectedBiome.evolve(Game.selectedSpecies, selectedFeature)
    println("You created new species: $newSpecies")

}


fun executeSpeciesCommand(commandArgument: String?) {
    if (null == commandArgument) {
        println(Game.selectedBiome)
        outputSpecies(Game.selectedBiome.species())
        return
    }
    val speciesToSelect = commandArgument.toInt()
    Game.selectSpecies(Game.selectedBiome.species()[speciesToSelect])
    println("Selected Biome: ${Game.selectedBiome}")
    println("Selected Species: ${Game.selectedSpecies}")
}

fun executeCommandBiome(commandArgument: String?) {
    if (null == commandArgument) {
        outputBiomes(Game.biomes())
        return
    }
    val biomeToSelect = commandArgument.toInt()
    Game.selectBiome(Game.biomes()[biomeToSelect])
    println("Selected Biome: ${Game.selectedBiome}")

}


fun outputFeatures(features: Iterable<Feature>) {
    features.withIndex()
        .map { "${it.index}\t ${it.value}" }
        .forEach { println(it) }
}

fun outputSpecies(species: Iterable<Species>) {
    species.withIndex()
        .map { "${it.index}\t ${it.value}" }
        .forEach { println(it) }
}

fun outputBiomes(biomes: Iterable<Biome>) {
    biomes.withIndex()
        .map { "${it.index}\t ${it.value}" }
        .forEach { println(it) }

}