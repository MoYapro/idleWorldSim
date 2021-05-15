package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Level
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test

internal class CalculateConsumeFactorTest {

    @Test
    fun countersReducePowerFactor() {
        val producerWithoutConter: ResourceProducer = Species("producerWithoutCounter", Feature(Meaty()))
        val producerWithCounter: ResourceProducer = Species("producerWithCounter", Feature(Meaty(), Stealth()))
        val consumer: ResourceConsumer = Species("consumer", Feature(Predator(Meaty()), Vision()))
        assertThat(producerWithCounter.getCounters(consumer.traits())).isNotEmpty
        val powerFactorWithCounter = consumer.consumePowerFactor(producerWithCounter)
        val powerFactorWithoutCounter = consumer.consumePowerFactor(producerWithoutConter)
        assertThat(powerFactorWithoutCounter).isGreaterThan(powerFactorWithCounter)
    }

    @Test
    fun countersReduceConsumerPreference() {
        val producerWithoutConter: ResourceProducer = Species("producerWithoutCounter", Feature(Meaty()))
        val producerWithCounter: ResourceProducer = Species("producerWithCounter", Feature(Meaty(), Stealth()))
        val consumer: ResourceConsumer = Species("consumer", Feature(Predator(Meaty()), Vision()))
        val factorWithCounter = consumer.calculatePreferenceIndex(producerWithCounter, consumer.consumePowerFactor(producerWithCounter))
        val factorWithoutCounter = consumer.calculatePreferenceIndex(producerWithoutConter, consumer.consumePowerFactor(producerWithoutConter))
        assertThat(factorWithoutCounter).isGreaterThan(factorWithCounter)
    }

    @Test
    fun levelDifferenceToFactor() {
        val species = Species("default")
        assertThat(species.levelDifferenceToFactor(Level(1), Level(1))).`as`("Equal level should result in 50% chance").isEqualTo(0.5, Offset.offset(0.0001))
        assertThat(species.levelDifferenceToFactor(Level(1), Level(0))).`as`("No counter should result in max chance").isEqualTo(0.99, Offset.offset(0.0001))
        assertThat(species.levelDifferenceToFactor(Level(2), Level(0))).`as`("No counter should result in max chance").isEqualTo(0.99, Offset.offset(0.0001))
        assertThat(species.levelDifferenceToFactor(Level(1), Level(3))).`as`("Higher counter results in lower power").isLessThan(species.levelDifferenceToFactor(Level(1), Level(2)))
        assertThat(species.levelDifferenceToFactor(Level(2), Level(5))).`as`("Higher counter results in lower power").isLessThan(species.levelDifferenceToFactor(Level(2), Level(4)))
        assertThat(species.levelDifferenceToFactor(Level(2), Level(1))).`as`("Higher action results in higher power").isLessThan(species.levelDifferenceToFactor(Level(3), Level(1)))
        assertThat(species.levelDifferenceToFactor(Level(4), Level(2))).`as`("Higher action results in higher power").isLessThan(species.levelDifferenceToFactor(Level(5), Level(2)))
    }

    @Test
    fun calculatePreferenceIndex() {
        val consumer = Species("consumer", Feature(Predator(Meaty())))
        val producer = Species("producer", Feature(Meaty()))
        val noProducer = Species("producer")
        assertThat(consumer.calculatePreferenceIndex(noProducer, consumer.consumePowerFactor(producer))).`as`("Index is 0 when producer cannot be consumed").isEqualTo(0.0)
        assertThat(consumer.calculatePreferenceIndex(producer, consumer.consumePowerFactor(noProducer))).`as`("Index is > 1 when producer can be consumed").isGreaterThan(1.0)
    }


}