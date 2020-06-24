package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.Trait

open class SpeciesImpl(
    override val name: String,
    override val features: List<Feature> = emptyList()
) : ProducerAndConsumer {
    constructor(name: String, vararg features: Feature) : this(name, listOf(*features))

    override val creator = { name: String, features: List<Feature> -> SpeciesImpl(name, features) }
    private val canConsume: MutableList<String> = mutableListOf()
    override fun canConsume(producer: ResourceProducer): Boolean {
        return canConsume.contains(producer.name)
    }

    override fun consumePowerFactor(producer: ResourceProducer): Double {
        return 0.4
    }

    override fun equals(other: Any?): Boolean {
        return if (null == other || other !is SpeciesImpl) {
            false
        } else {
            this.name == other.name
                    && this.traits() == other.traits()

        }
    }

    override fun hashCode(): Int {
        return name.hashCode() * 13 + traits().sumBy { it.hashCode() * 23 }
    }

    fun canConsume(producerName: String): ProducerAndConsumer {
        canConsume += producerName
        return this
    }

    override fun get(trait: Trait): List<Trait> {
        return emptyList()
    }
}
