package de.moyapro.idle.domain.skillTree

import de.moyapro.idle.domain.traits.Feature

/**
 * TreeOfLife representing the large scale features in the game.
 * These Features are independed of each other only having other feature as requirements.
 * More refinded evolution of more dependend feaures is implemented in bonsai tree with interdependend feaures
 */
class TreeOfLife(root: Feature = Feature()) {
    fun branchInto(feature: Feature): TreeOfLife {
        return TreeOfLife(feature)
    }

    fun getEvolvableFeaures(feature: Feature): Set<Feature> {
        return setOf()
    }
}

