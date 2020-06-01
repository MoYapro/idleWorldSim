@file:Suppress("FunctionName")

package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.Level

abstract class Trait internal constructor(val level: Level = Level(1))


object Meaty : Trait()


/**
 * Get all consumer's traits that counter given traits
 */
fun getCounters(traits: List<Trait>): List<Trait> {
    return emptyList()
}
