package de.moyapro.idleworldsim.util

import kotlin.reflect.KFunction2


inline fun <reified T : Any, V : Any, reified X : T> Collection<T>.applyTo(initialValue: V, action: KFunction2<X, V, V>): V {
    return this.filterIsInstance<X>()
        .fold(initialValue)
        { modifiedValue, listElement: X -> action.invoke(listElement, modifiedValue) }
}


inline fun <T> Iterable<T>.sumUsing(sumFunction: (T, T) -> T, zeroElementProvider: () -> T? = { null }): T? {
    return if (this is Collection<*> && this.size <= 0) return zeroElementProvider.invoke()
    else this.reduce(sumFunction)
}

