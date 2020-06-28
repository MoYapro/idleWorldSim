package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.CatchTrait
import de.moyapro.idleworldsim.domain.traits.FindTrait
import de.moyapro.idleworldsim.domain.traits.KillTrait
import de.moyapro.idleworldsim.domain.traits.Trait
import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.sum
import kotlin.math.pow
import kotlin.reflect.KClass


interface ResourceConsumer : Species {
    val minimumFactor: Double
        get() = 0.01

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

    fun calculatePreferenceIndex(producer: ResourceProducer): Int {
        val index = 1;
        /*
        + resources gained relative to others
        + number of positive traits gotten from consuming that producer
        - number of negative traits gotten from consuming that producer
         */
        return Integer.max(index, 0)
    }


    fun levelDifferenceToFactor(producer: ResourceProducer, trait: KClass<out Trait>) =
        levelDifferenceToFactor(sum(producer[trait]), sum(producer.getCounters(this[trait])))


    fun levelDifferenceToFactor(actionLevel: Level, counterLevel: Level): Double {
        val maxChance = 0.99
        val medianChance = 0.5
        val actionValue = actionLevel.level
        val counterValue = counterLevel.level
        val graphStretchFactor = 1.6
        return when {
            0 == counterValue -> maxChance
            actionValue == counterValue -> medianChance
            actionValue > counterValue -> 1 / (1 + graphStretchFactor.pow(-(actionValue - counterValue)))
            actionValue < counterValue -> 1 / (1 + graphStretchFactor.pow(-(actionValue - counterValue)))
            else -> throw IllegalStateException("Missed a case when calculating levelDifferenceToFactor for actionLevel: $actionLevel and counterLevel: $counterLevel")
        }
    }
}
