package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.traits.Feature

interface Species {
    val features: List<Feature>
    val name: String
    val creator: (name: String, features: List<Feature>) -> Species

    /**
     * Species evolves new feature(s) and a new species is created
     */
    fun evolveTo(vararg evolvedFeature: Feature, name: String? = null): Species {
        val newFeatures = mutableListOf(*evolvedFeature)
        newFeatures.addAll(features)
        val newName = name ?: "${this.name}+"
        return creator(newName, newFeatures)
    }

}
