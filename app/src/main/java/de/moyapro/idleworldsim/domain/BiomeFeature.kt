package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceProducer
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.Size

class BiomeFeature(override val name: String, override val features: List<Feature>) : ResourceProducer, TraitBearer {
    constructor(name: String, vararg features: Feature) : this(name, features.toList())


    override fun <T : TraitBearer> T.creator(): (String, Iterable<Feature>) -> TraitBearer {
        return { name: String, features: Iterable<Feature> -> BiomeFeature(name, features.toList()) }
    }

    override fun toString(): String {
        return "BiomeFeature[$name]"
    }

    override fun asTraitBearer(): TraitBearer {
        return this
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun getResourcesPerInstance(): Resources =
        traits()
            .map { it.getConsumptionResources(this[Size::class].firstOrNull()) }
            .reduceOrNull { resources1, resources2 -> resources1 + resources2 }
            ?: emptyResources()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BiomeFeature

        if (name != other.name) return false
        if (features != other.features) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + features.hashCode()
        return result
    }
}
