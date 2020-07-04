package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceFactor
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

@Suppress("UsePropertyAccessSyntax")
internal class ResourcesTest {
    @Test
    fun resourceCanProvideAll() {
        assertThat(Resources().canProvide(Resources()).all { it.value }).isTrue()
    }

    @Test
    fun cannotProvideWater() {
        assertThat(
            Resources()
                .setQuantity(Resource(Water, 1.0)).canProvide(Resources().setQuantity(Resource(Water, 2.0)))
                .all { it.value }
        ).isFalse()
    }


    @Test
    fun cannotProvideEnergy() {
        assertThat(
            Resources()
                .setQuantity(Resource(Energy, 1.0)).canProvide(Resources().setQuantity(Resource(Energy, 2.0)))
                .all { it.value }
        ).isFalse()
    }

    @Test
    fun cannotProvideMinerals() {
        assertThat(
            Resources()
                .setQuantity(Resource(Minerals, 1.0)).canProvide(Resources().setQuantity(Resource(Minerals, 2.0)))
                .all { it.value }
        ).isFalse()
    }

    @Test
    fun canProvideWater() {
        assertThat(
            Resources()
                .setQuantity(Resource(Water, 10.0)).canProvide(Resources().setQuantity(Resource(Water, 2.0)))
                .all { it.value }
        ).isTrue()
    }

    @Test
    fun canProvideMinerals() {
        assertThat(
            Resources()
                .setQuantity(Resource(Minerals, 10.0)).canProvide(Resources().setQuantity(Resource(Minerals, 2.0)))
                .all { it.value }
        ).isTrue()
    }

    @Test
    fun canProvideEnergy() {
        assertThat(
            Resources(Energy, 10.0)
                .canProvide(Resources().setQuantity(Resource(Energy, 2.0)))
                .all { it.value }
        )
            .isTrue()
    }

    @Test
    fun plusSingle() {
        assertThat(
            Resources(Water, 1.0)
                    + Resources(Water, 2.1)
        ).isEqualTo(
            Resources(Water, 3.1)
        )
    }

    @Test
    fun plusMultiple() {
        val initialResourcesList = listOf(Resource(Water, 9.112), Resource(Minerals, 4.123))
        val additionalResourcesList = listOf(Resource(Water, 0.888), Resource(Oxygen, 6.612))
        val expectedAddition = listOf(Resource(Water, 10.0), Resource(Minerals, 4.123), Resource(Oxygen, 6.612))
        assertThat(
            Resources(initialResourcesList) + Resources(additionalResourcesList)
        ).isEqualTo(
            Resources(expectedAddition)
        )
    }

    @Test
    fun singleConstructor() {
        val setQuantity = 3.14159
        val testResources = Resources(Minerals, setQuantity)
        assertThat(testResources[Minerals].amount).isEqualTo(setQuantity)
        assertThat(testResources[Water].amount).isEqualTo(0.0)
        assertThat(testResources.quantities.size).`as`("Init with one resource should contain only that resource").isEqualTo(1)
    }


    @Test
    fun minusSingle() {
        assertThat(
            Resources(Water, 10.0) - Resources(Water, 5.1)
        ).isEqualTo(
            Resources(Water, 4.9)
        )
    }

    @Test
    fun plusSpecies() {
        val theSpecies = Species("species1")
        val initialSpeciesPopulation = Resources(theSpecies, Population(5.0))
        val additionalSpeciesPopulation = Resources(theSpecies, Population(5.0))
        assertThat((initialSpeciesPopulation + additionalSpeciesPopulation)[theSpecies])
            .isEqualTo(Population(10.0))
    }

    @Test
    fun minusSpecies() {
        val theSpecies = Species("species1")
        val initialSpeciesPopulation = Resources(theSpecies, Population(7.0))
        val toRemovSpeciesPopulation = Resources(theSpecies, Population(5.0))
        assertThat((initialSpeciesPopulation - toRemovSpeciesPopulation)[theSpecies])
            .isEqualTo(Population(2.0))
    }


    @Test
    fun minusFromEmptyResources() {
        assertThat(
            emptyResources() - Resources(Water, 5.1)
        ).isEqualTo(
            Resources(Water, -5.1)
        )
    }

    @Test
    fun timesScalar() {
        assertThat(Resources(doubleArrayOf(5.0, 5.0, 5.0, 5.0)) * 3.0)
            .isEqualTo(Resources(doubleArrayOf(15.0, 15.0, 15.0, 15.0)))
    }

    @Test
    fun timesResourceFactors() {
        assertThat(
            Resources(
                doubleArrayOf(
                    5.0,
                    5.0,
                    5.0,
                    5.0
                )
            ) * ResourceFactor(3.0, 3.0, 3.0, 3.0)
        )
            .isEqualTo(Resources(doubleArrayOf(15.0, 15.0, 15.0, 15.0)))
    }

    @Test
    fun createResourcesWithMap() {
        assertThat(
            Resources(
                listOf(Resource(Energy, 0.0))
            )
        ).isNotNull
    }

    @Test
    fun setQuantity() {
        val theValue = Resource(Water, 666.3)
        assertThat(Resources().setQuantity(theValue)[Water]).isEqualTo(theValue)
    }

    @Test
    fun setDefaultQuantity() {
        assertThat(Resources().setQuantity(Resource(Water))[Water].amount).isEqualTo(1.0)
    }

    @Test
    fun setDefaultPolulation() {
        val theSpecies = Species("X")
        assertThat(Resources().setPopulation(theSpecies)[theSpecies]).isEqualTo(Population(1.0))
    }

    @Test
    fun setPolulation() {
        val theValue = Population(345.678)
        val theSpecies = Species("X")
        assertThat(Resources().setPopulation(theSpecies, theValue)[theSpecies]).isEqualTo(theValue)
    }

    @Test
    fun hashCodeEquals() {
        val resources1 = Resources(listOf(Resource(Water, 4.1), Resource(Minerals, 13.0)))
        val resources2 = Resources(listOf(Resource(Minerals, 13.0), Resource(Water, 4.1)))
        assertThat(resources1).isEqualTo(resources2)
        assertThat(resources1.hashCode()).isEqualTo(resources2.hashCode())
    }

    @Test
    fun hashCodeNotEquals() {
        val resources1 = Resources().setQuantity(Resource(Minerals, 99.0))
        val resources2 = Resources().setQuantity(Resource(Water, 2.0))
        assertThat(resources1).isNotEqualTo(resources2)
        assertThat(resources1.hashCode()).isNotEqualTo(resources2.hashCode())
    }

    @Test
    fun wrongResourceAssignThrowsException() {
        val exception = assertFailsWith<IllegalArgumentException> { Resources()[Water] = Resource(Minerals, 1.0) }
        assertThat(exception).`as`("Should produce exception").isNotNull()
    }


}
