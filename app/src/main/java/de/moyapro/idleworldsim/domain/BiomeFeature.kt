package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.traits.Feature

class BiomeFeature(override val name: String, override val features: List<Feature>) : ResourceProducer {
    constructor(name: String, vararg features: Feature) : this(name, features.toList())


    override fun <T : TraitBearer> T.creator(): (String, Iterable<Feature>) -> T {
        return { name: String, features: Iterable<Feature> -> BiomeFeature(name, features.toList()) as T }
    }

}
