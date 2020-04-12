package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.consumption.Resource.Energy
import de.moyapro.idle.domain.consumption.Resource.Oxygen

/**
 * Just a few predefined features
 */


fun sunlightConsumer(): Feature {
    return Feature("SunlightConsumer", mutableSetOf(ProduceResource(Oxygen), ConsumerTrait(Energy), NeedResource(Energy)))
}

fun oxygenConsumer(): Feature {
    return Feature("OxygenConsumer", mutableSetOf(ConsumerTrait(Oxygen), NeedResource(Oxygen)))
}
