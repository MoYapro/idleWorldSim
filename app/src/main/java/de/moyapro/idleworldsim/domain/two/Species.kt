package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.traits.Feature

interface Species {
    val features: List<Feature>
    val name: String

    /**
     * Species evolves new feature(s) and a new species is created
     */
    fun evolveTo(vararg evolvedFeature: Feature): Species

}
