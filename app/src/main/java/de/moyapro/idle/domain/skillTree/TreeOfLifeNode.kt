package de.moyapro.idle.domain.skillTree

import de.moyapro.idle.domain.traits.Feature

/**
 * A (general) Feature a species can evolve
 */
class TreeOfLifeNode<in T : Feature>(before: T) {
    fun <R : Feature> branchInto(feature: R): TreeOfLifeNode<R> {
        return TreeOfLifeNode(feature)
    }

    fun extendBranch(feature2: T): TreeOfLifeNode<T> {
        return this
    }
}