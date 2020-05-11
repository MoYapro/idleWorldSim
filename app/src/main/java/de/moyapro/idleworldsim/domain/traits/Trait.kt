@file:Suppress("FunctionName")

package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.valueObjects.Level

abstract class Trait internal constructor(val level: Level = Level(1))


object Meaty : Trait()
