@file:Suppress("FunctionName")

package de.moyapro.idle.domain.traits

abstract class Trait internal constructor(val level: Int = 1)

inline fun <reified T : Trait, V : Any> List<Trait>.applyTo(initialValue: V, influenceMethod: T.(V) -> V): V {
    return this.filterIsInstance<T>()
        .fold(initialValue)
        { modifiedValue, trait -> influenceMethod.invoke(trait, modifiedValue) }
}
