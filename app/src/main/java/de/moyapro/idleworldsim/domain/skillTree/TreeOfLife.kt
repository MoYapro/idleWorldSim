package de.moyapro.idleworldsim.domain.skillTree

import de.moyapro.idleworldsim.domain.traits.Feature

class TreeOfLifeBuilder<T : Feature>(private val parent: T, private val tree: TreeOfLife<T>) {
    fun branchInto(child: T, block: (TreeOfLifeBuilder<T>.() -> Unit)?) {
        tree.add(parent, child)
        block?.invoke(TreeOfLifeBuilder(child, tree))
    }

    fun branchInto(child: T) {
        tree.add(parent, child)
    }

    fun create(): TreeOfLife<T> {
        return tree
    }
}

/**
 * TreeOfLife representing the large scale features in the game.
 * These Features are independent of each other only having other feature as requirements.
 * More refined evolution of more dependent features is implemented in bonsai tree with interdependent features
 */
class TreeOfLife<T : Feature> {
    companion object {
        fun <T : Feature> build(root: T, block: (TreeOfLifeBuilder<T>.() -> Unit)?): TreeOfLife<T> {
            return TreeOfLifeBuilder(root, TreeOfLife()).let {
                block?.invoke(it)
                it
            }.create()
        }

        fun <T : Feature> build(root: T): TreeOfLife<T> {
            return TreeOfLifeBuilder(root, TreeOfLife()).create()
        }
    }

    private var evolutions: MutableMap<T, MutableSet<T>> = mutableMapOf()

    fun add(ancestor: T, descendant: T): TreeOfLife<T> {
        this.evolutions.getOrPut(ancestor, { mutableSetOf() }) += descendant
        return this
    }

    override fun toString(): String {
        var s = "["
        if (this.evolutions.isNotEmpty()) {
            s += " {" + this.evolutions.map { it.toString() } + " }"
        }
        s += "]"
        return s
    }

    fun getEvolvableFeatures(feature: Feature): Set<T> = this.evolutions[feature] ?: setOf()
}
