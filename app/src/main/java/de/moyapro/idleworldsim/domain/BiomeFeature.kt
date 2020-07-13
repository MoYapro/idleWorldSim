package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.Feature

class BiomeFeature(override val name: String, override val features: List<Feature>) : ResourceProducer {
    override fun getResourcesPerInstance(): Resources {
        return Resources()
    }

    override fun <T : TraitBearer> T.creator(): (String, Iterable<Feature>) -> T {
        return { name: String, features: Iterable<Feature> -> BiomeFeature(name, features.toList()) as T }
    }

}
