package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType

class AquireResource(val resourceType: ResourceType, level: Level = Level(1)) : Trait(level)
