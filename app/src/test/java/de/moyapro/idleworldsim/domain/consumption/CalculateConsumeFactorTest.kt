package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.traits.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CalculateConsumeFactorTest {

    @Test
    fun countersReducePowerFactor() {
        val producerWithoutConter: ResourceProducer = SpeciesImpl("producerWithoutCounter", Feature(Meaty))
        val producerWithCounter: ResourceProducer = SpeciesImpl("producerWithCounter", Feature(Meaty, Stealth()))
        val consumer: ResourceConsumer = SpeciesImpl("consumer", Feature(Predator(Meaty), Vision()))
        val powerFactorWithCounter = consumer.consumePowerFactor(producerWithCounter)
        val powerFactorWithoutCounter = consumer.consumePowerFactor(producerWithoutConter)
        assertThat(powerFactorWithCounter).isGreaterThan(powerFactorWithoutCounter)
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

}