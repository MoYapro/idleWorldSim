package de.moyapro.idle.util

import kotlin.reflect.KFunction2

inline fun <reified T : Any, V : Any, reified X : T> List<T>.applyTo(initialValue: V, influenceMethod: KFunction2<X, V, V>): V {
    var x = initialValue
    this.forEach {
        if (it is X) // since covariance is not allowed in extensionfunctions we use if to check if T is subclass of X
            x = influenceMethod.invoke(it, initialValue)
    }
    return x
}

// This is just for demonstration that kotlin compiler's type inference is still not perfect. Code should be semanticaly identical to working code above
//inline fun <reified T : Any, V : Any, reified X : T> List<T>.applyTo(initialValue: V, influenceMethod: KFunction2<X, V, V>): V {
//    return this.filterIsInstance<X>()
//        .fold(initialValue)
//        { modifiedValue, listElement: T -> influenceMethod.invoke(listElement, modifiedValue) }
//}
