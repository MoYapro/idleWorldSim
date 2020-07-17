package de.moyapro.idleworldsim.cli

import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.consumption.FoodChainEdge
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.util.toShortDecimalStr
import java.util.*
import kotlin.system.exitProcess

fun main() {
    println("Welcome")
    println(Game.help)
    Game.runSimulation()
    handleUserInput()
}

private fun handleUserInput() {
    while (true) {
        val split = readLine()!!.split(' ')
        val command = split.first()
        val commandArgument: String? = if (split.size > 1) split[1] else null
        when (command.toUpperCase(Locale.getDefault())) {
            "S" -> executeSpeciesCommand(commandArgument)
            "R" -> executeRelationCommand(commandArgument)
            "B" -> executeCommandBiome(commandArgument)
            "E" -> executeEvolveCommand(commandArgument)
            "H" -> println(Game.help)
            "Q" -> exitProcess(0)
        }
    }
}

fun executeRelationCommand(commandArgument: String?) {
    outputRelations(Game.speciesRelations())
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
    if (null == commandArgument || "" == commandArgument) {
        println(Game.selectedBiome)
        outputSpecies(Game.selectedBiome.population())
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

fun outputSpecies(speciesPopulation: Map<Species, Population>) {
    speciesPopulation.keys.withIndex()
        .map { species -> "${species.index}\t\t ${speciesPopulation[species.value]?.populationSize?.toShortDecimalStr()} \t\t ${species.value}" }
        .forEach { println(it) }
}

fun outputBiomes(biomes: Iterable<Biome>) {
    biomes.withIndex()
        .map { "${it.index}\t ${it.value}" }
        .forEach { println(it) }
}

fun outputRelations(relations: Iterable<FoodChainEdge>) {
    relations.forEach {
        println("${it.producer} \t\t->\t\t ${it.consumer} --- ${it.consumeFactor} --- ${it.consumerPreference}")
    }
}