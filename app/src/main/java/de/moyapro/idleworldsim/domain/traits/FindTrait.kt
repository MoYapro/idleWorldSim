package de.moyapro.idleworldsim.domain.traits

import kotlin.reflect.KClass

open class FindTrait(canCounter: Set<KClass<out Trait>> = emptySet()) : Trait(canCounter = canCounter)


open class Vision : FindTrait()
class SuperVision : Vision()



class Smell : FindTrait()
class Hearing : FindTrait()

// vvvvvvvvvvvvvvvvvvv some counters vvvvvvvvvvvvvvvvvvvvvv

open class Stealth : FindTrait(canCounter = setOf(Vision::class))
class SuperStealth : Stealth()
