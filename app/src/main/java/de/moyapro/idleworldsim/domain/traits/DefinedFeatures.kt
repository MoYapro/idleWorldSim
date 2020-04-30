package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Energy
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Oxygen

/**
 * Just a few predefined features
 */
fun Feature.Companion.sunlightConsumer(): Feature {
    return Feature("SunlightConsumer", mutableSetOf(ProduceResource(Oxygen), ConsumerTrait(Energy), NeedResource(Energy)))
}

fun Feature.Companion.oxygenConsumer(): Feature {
    return Feature("OxygenConsumer", mutableSetOf(ConsumerTrait(Oxygen), NeedResource(Oxygen)))
}
