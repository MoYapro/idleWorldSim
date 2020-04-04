package de.moyapro.idle.domain.skillTree

import de.moyapro.idle.domain.traits.Feature
import java.util.*

/**
 * TreeOfLife representing the large scale features in the game.
 * These Features are independed of each other only having other feature as requirements.
 * More refinded evolution of more dependend feaures is implemented in bonsai tree with interdependend feaures
 */
class TreeOfLife<T : Feature>(private var feature: T) {
    var parent: TreeOfLife<T>? = null
    var subtrees: MutableSet<TreeOfLife<T>> = mutableSetOf()
    fun branchInto(feature: T): TreeOfLife<T> {
        val subtree = TreeOfLife(feature)
        subtree.parent = this
        subtrees.add(subtree)
        return subtree
    }

    override fun toString(): String {
        var s = "$feature"
        if (subtrees.isNotEmpty()) {
            s += " {" + subtrees.map { it.toString() } + " }"
        }
        return s
    }

    fun getEvolvableFeaures(root: Feature): Set<T> {
        val subtree = findSubtreeWithFeature(root)
        return if (subtree.isPresent) {
            subtree.get().getAllEvolvableFeatures()
        } else {
            setOf()
        }
    }

    private fun getAllEvolvableFeatures(): MutableSet<T> {
        return subtrees.map { it.feature }.toMutableSet()
    }

    private fun findSubtreeWithFeature(searchedFeature: Feature): Optional<TreeOfLife<T>> {
        if (this.feature == searchedFeature) {
            return Optional.of(this)
        } else {
            for (subtree in subtrees) {
                val found = subtree.findSubtreeWithFeature(searchedFeature)
                if (found.isPresent) {
                    return found
                }
            }
        }
        return Optional.empty()
    }
}

