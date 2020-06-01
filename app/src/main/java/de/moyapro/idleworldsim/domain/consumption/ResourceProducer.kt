package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Trait

interface ResourceProducer {
    val name: String

    /**
     * Get all traits or subtraits of this producer
     */
    operator fun get(trait: Trait): List<Trait>

}
