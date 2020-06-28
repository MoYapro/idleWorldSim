package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Level
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test

class CalculateConsumeFactorTest {

    @Test
    fun countersReducePowerFactor() {
        val producerWithoutConter: ResourceProducer = SpeciesImpl("producerWithoutCounter", Feature(Meaty))
        val producerWithCounter: ResourceProducer = SpeciesImpl("producerWithCounter", Feature(Meaty, Stealth()))
        val consumer: ResourceConsumer = SpeciesImpl("consumer", Feature(Predator(Meaty), Vision()))
        assertThat(producerWithCounter.getCounters(consumer.traits())).isNotEmpty
        val powerFactorWithCounter = consumer.consumePowerFactor(producerWithCounter)
        val powerFactorWithoutCounter = consumer.consumePowerFactor(producerWithoutConter)
        assertThat(powerFactorWithoutCounter).isGreaterThan(powerFactorWithCounter)
    }

    @Test
    fun countersReduceConsumerPreference() {
        val producerWithoutConter: ResourceProducer = SpeciesImpl("producerWithoutCounter", Feature(Meaty))
        val producerWithCounter: ResourceProducer = SpeciesImpl("producerWithCounter", Feature(Meaty, Stealth()))
        val consumer: ResourceConsumer = SpeciesImpl("consumer", Feature(Predator(Meaty), Vision()))
        val factorWithCounter = consumer.calculatePreferenceIndex(producerWithCounter)
        val factorWithoutCounter = consumer.calculatePreferenceIndex(producerWithoutConter)
        assertThat(factorWithCounter).isGreaterThan(factorWithoutCounter)
    }

    @Test
    fun levelDifferenceToFactor() {
        val species = SpeciesImpl("default")
        assertThat(species.levelDifferenceToFactor(Level(1), Level(1))).`as`("Equal level should result in 50% chance").isEqualTo(0.5, Offset.offset(0.0001))
        assertThat(species.levelDifferenceToFactor(Level(1), Level(0))).`as`("No Counter should result in max chance").isEqualTo(0.99, Offset.offset(0.0001))
        assertThat(species.levelDifferenceToFactor(Level(2), Level(0))).`as`("No Counter should result in max chance").isEqualTo(0.99, Offset.offset(0.0001))

    }

}