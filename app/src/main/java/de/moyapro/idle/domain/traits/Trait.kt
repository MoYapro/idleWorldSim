@file:Suppress("FunctionName")

package de.moyapro.idle.domain.traits

abstract class Trait internal constructor(val level: Int = 1) {
    override fun toString(): String {
        return this::class.simpleName ?: "GenericTrait"
    }
}
