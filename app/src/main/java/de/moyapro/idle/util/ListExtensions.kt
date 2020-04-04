package de.moyapro.idle.util

import kotlin.reflect.KFunction2

inline fun <reified T : Any, V : Any, reified X : T> Collection<T>.applyTo(initialValue: V, influenceMethod: KFunction2<X, V, V>): V {
    return this.filterIsInstance<X>()
        .fold(initialValue)
        { modifiedValue, listElement: X -> influenceMethod.invoke(listElement, modifiedValue) }
}
