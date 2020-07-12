package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.Trait
import kotlin.reflect.KClass

interface TraitBearer {
    val name: String
    val features: List<Feature>

    /**
     * get all traits this traitbearer has
     */
    fun traits(): Iterable<Trait> = this.features.map { it.getTraits() }.flatten()

    /**
     * Get all consumer's traits that counter given traits
     */
    fun getCounters(traits: Iterable<Trait>): Iterable<Trait> =
        this.traits()
            .filter { consumerTrait -> traits.any { consumerTrait.canCounter(it) } }

    /**
     * get list of traits that are the given traitClass or subclass it
     */
    operator fun get(traitClass: KClass<out Trait>): Iterable<Trait> =
        traits()
            .filterIsInstance(traitClass.javaObjectType)

    fun <T: TraitBearer> T.creator(): (String, Iterable<Feature>) -> T


}

/**
 * Traitbaerer evolves new feature(s) and a new traitbaerer is created
 *
 * This needs to be an extension function to be able to return type of instance on which it was called.
 */
fun <T : TraitBearer> T.evolveTo(vararg evolvedFeature: Feature, name: String? = null): T {
    val newFeatures = mutableListOf(*evolvedFeature)
    newFeatures.addAll(features)
    val newName = name ?: "${this.name}+"
    return creator<T>().invoke(newName, newFeatures) as T
}

