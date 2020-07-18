package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
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
    fun differentSpeciesAreDifferent() {
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
        val species = Species("Testsubject", Feature(Vision(), SuperVision(), Hearing(), Predator(Meaty())))
        assertThat(species[FindTrait::class]).isEqualTo(listOf(Vision(), SuperVision(), Hearing()))
    }

    @Test
    fun getCounters() {
        val traitsToCounter = listOf(Vision(), Hearing(), Predator(Meaty()))
        val producer = Species("Producer", Feature(Stealth(), Meaty(), Smell()))
        assertThat(producer.getCounters(traitsToCounter)).isEqualTo(listOf(Stealth()))
    }

    @Test
    fun speciesShouldShrinkOnResourceShortage() {
        val initialSize = 10.0
        val species = Species("I", Feature(NeedResource(Water), NeedResource(Minerals), ConsumerTrait(Water), ConsumerTrait(Minerals)))
        Assertions.assertThat(
            species.consume(Population(initialSize), Resources(Resource(Oxygen, 1000))).populationSize
        )
            .isLessThan(initialSize)
    }

    @Test
    fun speciesShouldGrowOnResourceSurplus() {
        val initialSize = 10.0
        val species = Species("I", Feature(NeedResource(Water), ConsumerTrait(Water)))
        val availableResources = Resources(Resource(Water, 1000))
        Assertions.assertThat(
            species.consume(Population(initialSize), availableResources).populationSize
        )
            .isGreaterThan(initialSize)
    }


    @Test
    fun speciesResourcesPerInstance_zero_size0_noTraits() {
        assertThat(
            Species("tiny")
                .getResourcesPerInstance()
        ).isEqualTo(emptyResources())
    }

    @Test
    fun speciesResourcesPerInstance_one_size1_noTraits() {
        assertThat(
            Species("small", Feature(Size(1)))
                .getResourcesPerInstance()
        )
            .isEqualTo(
                Resources(
                    ResourceType.values()
                        .map { Resource(it, 1) }
                )
            )
    }

    @Test
    fun speciesResourcesPerInstance_size1_meatyTraits() {
        assertThat(
            Species("small", Feature(Size(1), Meaty(Level(1))))
                .getResourcesPerInstance()
        )
            .isEqualTo(
                Resources(
                    ResourceType.values()
                        .map { Resource(it, 11) }
                )
            )
    }

    @Test
    fun speciesResourcesPerInstance_size2_meatyTraits() {
        assertThat(
            Species("small", Feature(Size(2), Meaty(Level(1))))
                .getResourcesPerInstance()
        )
            .isEqualTo(
                Resources(
                    ResourceType.values()
                        .map { Resource(it, 22) }
                )
            )
    }

    @Test
    fun speciesResourcesPerInstance_size2_meatyTraitsWithScaling() {
        assertThat(
            Species("small", Feature(Size(1), Meaty(Level(2))))
                .getResourcesPerInstance()
        )
            .isEqualTo(
                Resources(
                    ResourceType.values()
                        .map { Resource(it, 21) }
                )
            )
    }

    @Test
    fun speciesResourcesPerInstance_size2_meatyTraitsWithSizeScaling() {
        assertThat(
            Species("small", Feature(Size(2), Meaty(Level(2))))
                .getResourcesPerInstance()
        )
            .isEqualTo(
                Resources(
                    ResourceType.values()
                        .map { Resource(it, 42) }
                )
            )
    }

    @Test
    fun newTraitCanRemoveOldTrait() {

    }

    @Test
    fun traitsCanBeUpgraded() {

    }


}
