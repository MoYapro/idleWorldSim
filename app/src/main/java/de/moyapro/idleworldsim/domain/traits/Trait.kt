@file:Suppress("FunctionName")

package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.Level
import kotlin.reflect.KClass

abstract class Trait internal constructor(
    val level: Level = Level(1),
    private val canCounter: Collection<KClass<out Trait>> = emptySet()
) {
    fun canCounter(otherTrait: Trait): Boolean {
        val otherTraitClass: KClass<out Trait> = otherTrait::class
        return canCounter
            .asSequence()
            .any { otherTraitClass == it }
    }

    override fun equals(other: Any?) = when {
        null == other -> false
        other !is Trait -> false
        this::class != other::class -> false
        else -> this.level == other.level && this::class == other::class
    }

    override fun hashCode(): Int {
        return super.hashCode() * 37 + this.level.level * 67 + this::class.hashCode()
    }

}

object Meaty : Trait()