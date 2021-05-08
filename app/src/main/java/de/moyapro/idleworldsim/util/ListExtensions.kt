package de.moyapro.idleworldsim.util

import de.moyapro.idleworldsim.domain.valueObjects.Population
import kotlin.reflect.KFunction2


inline fun <reified T : Any, V : Any, reified X : T> Collection<T>.applyTo(
    initialValue: V,
    action: KFunction2<X, V, V>
): V {
    return this.filterIsInstance<X>()
        .fold(initialValue)
        { modifiedValue, listElement: X -> action.invoke(listElement, modifiedValue) }
}


inline fun <T> Iterable<T>.sumUsing(sumFunction: (T, T) -> T, zeroElementProvider: () -> T? = { null }): T? {
    return if (this is Collection<*> && this.size <= 0) return zeroElementProvider.invoke()
    else this.reduce(sumFunction)
}

operator fun <T> Map<T, Population>.plus(other: Map<T, Population>): Map<T, Population> {

    val sumExisting = this.map { (key, population) -> Pair(key, population + (other[key] ?: Population(0.0))) }
        .toMap()
        .toMutableMap()
    other
        .filter { (key, _) -> null == this[key] }
        .forEach { (key, value) -> sumExisting[key] = value }

    return sumExisting
}

operator fun <T, X : T> Map<T, Population>.minus(other: Map<X, Population>): Map<T, Population> {
    require(other.all { (key, value) -> value.isEmpty() || null != this[key] }) { "Cannot subtract if element is not in original map" }
    return this.map { (key, value) ->
        val newValue = value - (other[key] ?: Population(0.0))
        Pair(key, newValue)

    }
        .filter { (_, value) -> 0 < value.populationSize }
        .toMap()
}

