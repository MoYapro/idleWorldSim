package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.two.Species

open class BasicLife(override val name: String, override val features: List<Feature> = emptyList()) : Species {

    override fun evolveTo(vararg evolvedFeature: Feature): Species {
        TODO("Not yet implemented")
    }
}