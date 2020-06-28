package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.Predator

open class SpeciesImpl(
    override val name: String,
    override val features: List<Feature> = emptyList()
) : ProducerAndConsumer {
    constructor(name: String, vararg features: Feature) : this(name, listOf(*features))

    override val creator = { name: String, features: List<Feature> -> SpeciesImpl(name, features) }

    @Deprecated("This should not be used any more. CanConsume() should be based on traits")
    private val canConsume: MutableList<String> = mutableListOf()
    override fun canConsume(producer: ResourceProducer) =
        traits()
            .filterIsInstance<Predator>()
            .map { it.preyTrait }
            .any { producer.traits().contains(it) }

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

    @Deprecated("This should not be used any more. Can consume will be based on traits not on predetermined relation")
    fun canConsume(producerName: String): ProducerAndConsumer {
        canConsume += producerName
        return this
    }
}
