package de.moyapro.idle.util

import de.moyapro.idle.domain.traits.Trait

inline fun <reified T : Trait, V : Any> List<T>.applyTo(initialValue: V, influenceMethod: T.(V) -> V): V {
    return this.filterIsInstance<T>()
        .fold(initialValue)
        { modifiedValue, listElement: Trait -> influenceMethod.invoke(listElement, modifiedValue) }
}