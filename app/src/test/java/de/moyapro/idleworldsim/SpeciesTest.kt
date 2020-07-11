package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Minerals
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test


internal class SpeciesTest {

    @Test
    fun nullNotEqual() {
        assertThat(Species("any")).isNotEqualTo(null)
    }

    @Test
    fun otherTypeNotEqual() {
        assertThat(Species("any")).isNotEqualTo("any")
        assertThat(Species("any").hashCode()).isNotEqualTo("any".hashCode())
    }

    @Test
    fun defaultSpeciesShouldCreateDifferent() {
        val species1 = Species("one")
        val species2 = Species("two")
        assertThat(species1).isNotEqualTo(species2)
        assertThat(species1.hashCode()).isNotEqualTo(species2.hashCode())
    }

    @Test
    fun speciesWithDifferentTraitsAreDifferent() {
        val species1 = Species("any", Feature(ConsumerTrait(Minerals)))
        val species2 = Species("any", Feature(ConsumerTrait(Water)))
        assertThat(species1).isNotEqualTo(species2)
        assertThat(species1.hashCode()).isNotEqualTo(species2.hashCode())
    }

    @Test
    fun speciesWithSameTraitsAreEqual() {
        val species1 = Species("same", Feature(ConsumerTrait(Minerals)))
        val species2 = Species("same", Feature(ConsumerTrait(Minerals)))
        assertThat(species1).isEqualTo(species2)
        assertThat(species1.hashCode()).isEqualTo(species2.hashCode())
    }

    @Test
    fun equalSpecies() {
        val species1 = Species("same")
        val species2 = Species("same")
        assertThat(species1).isEqualTo(species2)
        assertThat(species1.hashCode()).isEqualTo(species2.hashCode())
    }

    @Test
    fun getTraits() {
        val species = Species("Testsubject", Feature(Vision(), SuperVision(), Hearing(), Predator(Meaty)))
        assertThat(species[FindTrait::class]).isEqualTo(listOf(Vision(), SuperVision(), Hearing()))
    }

    @Test
    fun getCounters() {
        val traitsToCounter = listOf(Vision(), Hearing(), Predator(Meaty))
        val producer = Species("Producer", Feature(Stealth(), Meaty, Smell()))
        assertThat(producer.getCounters(traitsToCounter)).isEqualTo(listOf(Stealth()))
    }

    @Test
    fun speciesShouldShrinkOnResourceShortage() {
        val initialSize = 10.0
        val species = Species("I", Feature(NeedResource(Water), ConsumerTrait(Water)))
        Assertions.assertThat(species.consume(Population(initialSize), emptyResources()).populationSize)
            .isLessThan(initialSize)
    }

    @Test
    fun speciesShouldGrowOnResourceSurplus() {
        val initialSize = 10.0
        val species = Species("I", Feature(NeedResource(Water), ConsumerTrait(Water)))
        Assertions.assertThat(species.consume(Population(initialSize), Resources()).populationSize)
            .isGreaterThan(initialSize)
    }

}
