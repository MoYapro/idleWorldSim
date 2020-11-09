package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.Trait
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.util.sumUsing
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
    operator fun <T : Trait> get(traitClass: KClass<out T>): Iterable<T> =
        traits()
            .filterIsInstance(traitClass.javaObjectType)

    fun getLevel(traitClass: KClass<out Trait>): Level =
        this[traitClass]
            .map { it.level }
            .sumUsing({ t1, t2 -> t1 + t2 }, { Level(0) })
            ?: Level(0)

    fun <T : TraitBearer> T.creator(): (String, Iterable<Feature>) -> TraitBearer


}

/**
 * Traitbaerer evolves new feature(s) and a new traitbaerer is created
 *
 * This needs to be an extension function to be able to return type of instance on which it was called.
 */
inline fun <reified T : TraitBearer> T.evolveTo(vararg evolvedFeature: Feature, name: String? = null): T {
    val newFeatures = mutableListOf(*evolvedFeature)
    // TODO: hier können auch features gedoppelt werden -> das geht besser
    newFeatures.addAll(features)
    val newName = name ?: "${this.name}+"
    // TODO: hässlichen Cast beseitigen
    return creator().invoke(newName, newFeatures) as T
}

