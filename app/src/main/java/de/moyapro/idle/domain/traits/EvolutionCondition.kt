package de.moyapro.idle.domain.traits

class EvolutionCondition(val required: Trait, val evolveTo: Trait)

class EvolutionConditions {
    private val list = mutableListOf<EvolutionCondition>()

    fun fulfilledBy(evolveTo: Trait, traits: MutableList<Trait>) =
        list.filter {
            it.evolveTo.javaClass == evolveTo.javaClass
        }.all { testTrait ->
            testTrait.required.javaClass in traits.map { it.javaClass }
        }

    fun add(condition: EvolutionCondition): EvolutionConditions {
        list.add(condition)
        return this
    }
}
