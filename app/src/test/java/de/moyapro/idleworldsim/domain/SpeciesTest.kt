package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Minerals
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
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
        val species1 = defaultSpecies()
        val species2 = defaultSpecies()
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

}
