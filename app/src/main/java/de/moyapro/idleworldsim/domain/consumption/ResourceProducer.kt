package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Trait
import de.moyapro.idleworldsim.domain.two.Species

interface ResourceProducer : Species {

    /**
     * Get all traits or subtraits of this producer
     */
    operator fun get(trait: Trait): List<Trait>

}
