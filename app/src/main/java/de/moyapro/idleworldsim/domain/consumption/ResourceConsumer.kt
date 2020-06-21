package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.CatchTrait
import de.moyapro.idleworldsim.domain.traits.FindTrait
import de.moyapro.idleworldsim.domain.traits.KillTrait
import de.moyapro.idleworldsim.domain.traits.Trait
import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.sum
import kotlin.math.max
import kotlin.reflect.KClass
import java.lang.Double.max as doubleMax

interface ResourceConsumer : Species {
    val minimumFactor: Double
        get() = 0.1

    fun canConsume(producer: ResourceProducer): Boolean

    /**
     * Calculate an index indicating how good the consume can find/hunt/eat the producer
     */
    fun consumePowerFactor(producer: ResourceProducer): Double {
        val findFactor = levelDifferenceToFactor(producer, FindTrait::class)
        val catchFactor = levelDifferenceToFactor(producer, CatchTrait::class)
        val killFactor = levelDifferenceToFactor(producer, KillTrait::class)
        return findFactor * catchFactor * killFactor
    }

    fun levelDifferenceToFactor(producer: ResourceProducer, trait: KClass<out Trait>) =
        levelDifferenceToFactor(sum(producer[trait]), sum(getCounters(producer[trait])))


    fun levelDifferenceToFactor(actionLevel: Level, counterLevel: Level): Double {
        val differenceFactor = 1.0 / max((actionLevel - counterLevel).level, 1)
        return doubleMax(differenceFactor, minimumFactor)
    }

    /**
     * Get all consumer's traits that counter given traits
     */
    fun getCounters(traits: List<Trait>): List<Trait> {
        return this.traits()
            .filter { consumerTrait -> traits.any { consumerTrait.canCounter(it) } }

    }





}
