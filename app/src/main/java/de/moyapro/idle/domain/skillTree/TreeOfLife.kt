package de.moyapro.idle.domain.skillTree

import de.moyapro.idle.domain.traits.Feature

/**
 * TreeOfLife representing the large scale features in the game.
 * These Features are independent of each other only having other feature as requirements.
 * More refined evolution of more dependent features is implemented in bonsai tree with interdependent features
 */
class TreeOfLife<T : Feature>(private var feature: T) {
    private var children: MutableSet<TreeOfLife<T>> = mutableSetOf()

    fun branchInto(feature: T): TreeOfLife<T> {
        val child = TreeOfLife(feature)
        children.add(child)
        return child
    }

    override fun toString(): String {
        var s = "$feature"
        if (children.isNotEmpty()) {
            s += " {" + children.map { it.toString() } + " }"
        }
        return s
    }

    fun getEvolvableFeatures(feature: Feature): Set<T> = findSubtreeWithFeature(feature)?.getAllEvolvableFeatures() ?: setOf<T>()

    private fun getAllEvolvableFeatures(): MutableSet<T> = children.map { it.feature }.toMutableSet()

    private fun findSubtreeWithFeature(searchedFeature: Feature): TreeOfLife<T>? =
        if (this.feature === searchedFeature)
            this
        else
            children.firstMapped({ child -> child.findSubtreeWithFeature(searchedFeature) }) { it != null }
}

fun <T, R> Collection<T>.firstMapped(block: (T) -> R?, condition: (R?) -> Boolean): R? {
    forEach { el -> block.invoke(el).let { if (condition.invoke(it)) return it } }
    return null
}
