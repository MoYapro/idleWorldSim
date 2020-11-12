package de.moyapro.idleworldsim.cli

import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.util.toShortDecimalStr
import java.util.*

fun main() {
    println("Welcome")
    println(Game.help)
    Game.runSimulation()
    handleUserInput()
    println("Keep yourself evolving! ;)")
}

private fun handleUserInput() {
    while (Game.running) {
        val split = readLine()!!.split(' ')
        val command = split.first()
        val commandArgument: String? = if (split.size > 1) split[1] else null
        when (command.toUpperCase(Locale.getDefault())) {
            "S" -> executeSpeciesCommand(commandArgument)
            "R" -> executeRelationCommand(commandArgument)
            "B" -> executeCommandBiome(commandArgument)
            "E" -> executeEvolveCommand(commandArgument)
            "T" -> executeTraitCommand(commandArgument)
            "H" -> println(Game.help)
            "Q" -> Game.stopSimulation()
        }
    }
}

fun executeTraitCommand(commandArgument: String?) {
    outputWithIndex(Game.getTraitsOfSelectedSpecies())
}


fun executeRelationCommand(commandArgument: String?) {
    outputWithIndex(Game.speciesRelations())
}

fun executeEvolveCommand(commandArgument: String?) {
    if (null == commandArgument || "" == commandArgument) {
        println(Game.selectedBiome)
        println(Game.selectedSpecies)
        outputWithIndex(Game.getEvolveOptions())
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
    if (null == commandArgument || "" == commandArgument) {
        outputWithIndex(Game.biomesList())
        return
    }
    val biomeToSelect = commandArgument.toInt()
    Game.selectBiome(Game.biomesList()[biomeToSelect])
    println("Selected Biome: ${Game.selectedBiome}")

}

fun outputWithIndex(traits: Iterable<Any>) {
    traits.withIndex()
        .map { "${it.index}\t ${it.value}" }
        .forEach { println(it) }
}

fun outputSpecies(speciesPopulation: Map<Species, Population>) {
    speciesPopulation.keys.withIndex()
        .map { species -> "${species.index}\t\t ${speciesPopulation[species.value]?.populationSize?.toShortDecimalStr()} \t\t ${species.value}" }
        .forEach { println(it) }
}
