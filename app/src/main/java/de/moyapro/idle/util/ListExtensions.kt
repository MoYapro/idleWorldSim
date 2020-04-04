package de.moyapro.idle.util

import kotlin.reflect.KFunction2

// This is just for demonstration that kotlin compiler's type inference is still not perfect. Code should be semantically identical to working code above
inline fun <reified T : Any, V : Any, reified X : T> Collection<T>.applyTo(initialValue: V, influenceMethod: KFunction2<X, V, V>): V {
    return this.filterIsInstance<X>()
        .fold(initialValue)
        { modifiedValue, listElement: X -> influenceMethod.invoke(listElement, modifiedValue) }
}
