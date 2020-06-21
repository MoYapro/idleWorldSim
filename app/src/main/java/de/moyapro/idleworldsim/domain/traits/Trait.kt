@file:Suppress("FunctionName")

package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.Level
import kotlin.reflect.KClass

abstract class Trait internal constructor(
    val level: Level = Level(1),
    private val canCounter: Collection<KClass<out Trait>> = emptySet()
) {
    fun canCounter(otherTrait: Trait): Boolean {
        val otherTraitClass:KClass<out Trait> = otherTrait::class
        return canCounter
            .asSequence()
            .any { otherTraitClass.isInstance(it::class) }
    }
}


object Meaty : Trait()