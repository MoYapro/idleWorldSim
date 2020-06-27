package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.consumption.SpeciesImpl
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Minerals
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test


internal class SpeciesTest {

    @Test
    fun nullNotEqual() {
        assertThat(SpeciesImpl("any")).isNotEqualTo(null)
    }

    @Test
    fun otherTypeNotEqual() {
        assertThat(SpeciesImpl("any")).isNotEqualTo("any")
        assertThat(SpeciesImpl("any").hashCode()).isNotEqualTo("any".hashCode())
    }

    @Test
    fun defaultSpeciesShouldCreateDifferent() {
        val species1 = SpeciesImpl("one")
        val species2 = SpeciesImpl("two")
        assertThat(species1).isNotEqualTo(species2)
        assertThat(species1.hashCode()).isNotEqualTo(species2.hashCode())
    }

    @Test
    fun speciesWithDifferentTraitsAreDifferent() {
        val species1 = SpeciesImpl("any", Feature(ConsumerTrait(Minerals)))
        val species2 = SpeciesImpl("any", Feature(ConsumerTrait(Water)))
        assertThat(species1).isNotEqualTo(species2)
        assertThat(species1.hashCode()).isNotEqualTo(species2.hashCode())
    }

    @Test
    fun speciesWithSameTraitsAreEqual() {
        val species1 = SpeciesImpl("same", Feature(ConsumerTrait(Minerals)))
        val species2 = SpeciesImpl("same", Feature(ConsumerTrait(Minerals)))
        assertThat(species1).isEqualTo(species2)
        assertThat(species1.hashCode()).isEqualTo(species2.hashCode())
    }

    @Test
    fun equalSpeciesImpl() {
        val species1 = SpeciesImpl("same")
        val species2 = SpeciesImpl("same")
        assertThat(species1).isEqualTo(species2)
        assertThat(species1.hashCode()).isEqualTo(species2.hashCode())
    }

    @Test
    fun getTraits() {
        val species = SpeciesImpl("Testsubject", Feature(Vision(), SuperVision(), Hearing(), Predator(Meaty) ))
        assertThat(species[FindTrait::class]).isEqualTo(listOf(Vision(), SuperVision(), Hearing()))
    }

    @Test
    fun getCounters() {
        val traitsToCounter = listOf(Vision(), Hearing(), Predator(Meaty))
        val producer = SpeciesImpl("Producer", Feature(Stealth(), Meaty, Smell()))
        assertThat(producer.getCounters(traitsToCounter)).isEqualTo(listOf(Stealth()))

    }

}
