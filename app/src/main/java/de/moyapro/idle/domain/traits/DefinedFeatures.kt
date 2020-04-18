package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.consumption.Resource.Energy
import de.moyapro.idle.domain.consumption.Resource.Oxygen

/**
 * Just a few predefined features
 */
fun Feature.Companion.sunlightConsumer(): Feature {
    return Feature("SunlightConsumer", mutableSetOf(ProduceResource(Oxygen), ConsumerTrait(Energy), NeedResource(Energy)))
}

fun Feature.Companion.oxygenConsumer(): Feature {
    return Feature("OxygenConsumer", mutableSetOf(ConsumerTrait(Oxygen), NeedResource(Oxygen)))
}