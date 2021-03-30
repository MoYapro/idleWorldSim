package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.ResourceFactor
import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.consumption.emptyResources
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class ResourcesTest {
    @Test
    fun resourceCanProvideAll() {
        assertThat(Resources().canProvide(Resources()).all { it.value }).isTrue()
    }

    @Test
    fun plusSingle() {
        assertThat(
            Resources(Resource(Water, 1.0))
                    + Resources(Resource(Water, 2.1))
        ).isEqualTo(
            Resources(Resource(Water, 3.1))
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
        val testResources = Resources(Resource(Minerals, setQuantity))
        assertThat(testResources[Minerals].amount).isEqualTo(setQuantity)
        assertThat(testResources[Water].amount).isEqualTo(0.0)
        assertThat(testResources.quantities.size).`as`("Init with one resource should contain only that resource")
            .isEqualTo(1)
    }


    @Test
    fun minusSingle() {
        assertThat(
            Resources(Resource(Water, 10.0))
                    - Resources(Resource(Water, 5.1))
        ).isEqualTo(
            Resources(Resource(Water, 4.9))
        )
    }

    @Test
    fun resourcesCannotBeNegative() {
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { emptyResources() - Resources(Resource(Water, 5.1)) }
    }

    @Test
    fun timesScalar() {
        assertThat(
            Resources(values().map { Resource(it, 5.0) }) * 3.0
        )
            .isEqualTo(
                Resources(values().map { Resource(it, 15.0) })
            )
    }

    @Test
    fun timesResourceFactors() {
        assertThat(
            Resources(values().map { Resource(it, 5.0) })
                    * ResourceFactor(3.0, 3.0, 3.0, 3.0, oxygenFactor = 3.0, carbonFactor = 3.0)
        )
            .isEqualTo(
                Resources(values().map { Resource(it, 15.0) })
            )
    }

    @Test
    fun createResourcesWithMap() {
        assertThat(
            Resources(
                Resource(Energy, 0.0)
            )
        ).isNotNull
    }


    @Test
    fun hashCodeEquals() {
        val resources1 = Resources(Resource(Water, 4.1), Resource(Minerals, 13.0))
        val resources2 = Resources(Resource(Minerals, 13.0), Resource(Water, 4.1))
        assertThat(resources1).isEqualTo(resources2)
        assertThat(resources1.hashCode()).isEqualTo(resources2.hashCode())
    }

    @Test
    fun hashCodeNotEquals() {
        val resources1 = Resources(Resource(Minerals, 99.0))
        val resources2 = Resources(Resource(Water, 2.0))
        assertThat(resources1).isNotEqualTo(resources2)
        assertThat(resources1.hashCode()).isNotEqualTo(resources2.hashCode())
    }

    @Test
    fun minus() {
        val amount = Resource(Water, 10)
        val toRemove = Resource(Water, 3)
        val expected = Resource(Water, 7)
        assertThat(amount - toRemove).isEqualTo(expected)
    }

    @Test
    fun cannotCreateResourceWithNegativeAmount() {
        assertThatThrownBy { Resource(Energy, -1) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun minus_below_zero() {
        val amount = Resource(Water, 10)
        val toRemove = Resource(Water, 11)
        assertThatThrownBy { amount - toRemove }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun minus_wrongResourceType() {
        val amount = Resource(Water, 10)
        val toRemove = Resource(Energy, 1)
        assertThatThrownBy { amount - toRemove }.isInstanceOf(IllegalArgumentException::class.java)
    }


//    @Test
//    fun toList_empty() {
//        assertThat(Resources().toList()).isEmpty()
//    }

    @Test
    fun toList() {
        val resources = Resources(Resource(Water, 4.1), Resource(Minerals, 13.0))
        val expectedList = listOf(Resource(Water, 4.1), Resource(Minerals, 13.0))
        assertThat(resources.toList()).isEqualTo(expectedList)
    }
}
