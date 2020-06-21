package de.moyapro.idleworldsim.domain.traits

import kotlin.reflect.KClass

open class FindTrait(canCounter: Set<KClass<out Trait>> = emptySet()) : Trait(canCounter = canCounter)


class Vision : FindTrait()
class Smell : FindTrait()
class Hearing : FindTrait()

// vvvvvvvvvvvvvvvvvvv some counters vvvvvvvvvvvvvvvvvvvvvv

class Stealth : FindTrait(canCounter = setOf(Vision::class))
