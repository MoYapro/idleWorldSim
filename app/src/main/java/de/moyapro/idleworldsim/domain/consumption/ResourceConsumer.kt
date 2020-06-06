package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.sum
import kotlin.math.max
import java.lang.Double.max as doubleMax

interface ResourceConsumer {
    val name: String
    val minimumFactor: Double
        get() = 0.1

    fun canConsume(producer: ResourceProducer): Boolean

    /**
     * Calculate an index indicating how good the consume can find/hunt/eat the producer
     */
    fun consumePowerFactor(producer: ResourceProducer): Double {
        val findFactor = levelDifferenceToFactor(producer, FindTrait)
        val catchFactor = levelDifferenceToFactor(producer, CatchTrait)
        val killFactor = levelDifferenceToFactor(producer, KillTrait)
        return findFactor * catchFactor * killFactor
    }

    fun levelDifferenceToFactor(producer: ResourceProducer, trait: Trait) =
        levelDifferenceToFactor(sum(producer[FindTrait]), sum(getCounters(producer[FindTrait])))


    fun levelDifferenceToFactor(actionLevel: Level, counterLevel: Level): Double {
        val differenceFactor = 1.0 / max((actionLevel - counterLevel).level, 1)
        return doubleMax(differenceFactor, minimumFactor)
    }



    /**
     * get all traits of class or subclass of given trait in this consumer
     */
    operator fun get(trait: Trait): List<Trait>


}
