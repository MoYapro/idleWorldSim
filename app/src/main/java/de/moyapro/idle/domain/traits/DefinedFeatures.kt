package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.consumption.Resource.Energy
import de.moyapro.idle.domain.consumption.Resource.Oxygen

fun sunlightConsumer(): Feature {
    return Feature(OxygenProducer, ConsumerTrait(Energy))
}

fun oxygenConsumer(): Feature {
    return Feature(ConsumerTrait(Oxygen), ConsumerTrait(Energy))
}
