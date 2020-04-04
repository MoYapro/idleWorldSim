package de.moyapro.idle.util

import kotlin.reflect.KFunction2

inline fun <reified T : Any, V : Any, reified X : T> Collection<T>.applyTo(initialValue: V, action: KFunction2<X, V, V>): V {
    return this.filterIsInstance<X>()
        .fold(initialValue)
        { modifiedValue, listElement: X -> action.invoke(listElement, modifiedValue) }
}
