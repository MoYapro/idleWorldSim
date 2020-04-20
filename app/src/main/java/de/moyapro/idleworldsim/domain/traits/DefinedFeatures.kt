package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.consumption.Resource.Energy
import de.moyapro.idleworldsim.domain.consumption.Resource.Oxygen

/**
 * Just a few predefined features
 */
fun Feature.Companion.sunlightConsumer(): Feature {
    return Feature("SunlightConsumer", mutableSetOf(ProduceResource(Oxygen), ConsumerTrait(Energy), NeedResource(Energy)))
}

fun Feature.Companion.oxygenConsumer(): Feature {
    return Feature("OxygenConsumer", mutableSetOf(ConsumerTrait(Oxygen), NeedResource(Oxygen)))
}
