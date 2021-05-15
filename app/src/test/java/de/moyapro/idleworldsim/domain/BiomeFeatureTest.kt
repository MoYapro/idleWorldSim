package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.ProduceResource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Carbon
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Oxygen
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BiomeFeatureTest {
    @Test
    fun createBiomeFeature() {
        val air = BiomeFeature("Air", Feature(ProduceResource(Oxygen), ProduceResource(Carbon)))
        assertThat(air.traits()).contains(ProduceResource(Oxygen))
        assertThat(air.traits()).contains(ProduceResource(Carbon))
    }


    @Test
    fun equalsAndHashcode() {
        assertThat(BiomeFeature("X")).isNotEqualTo(BiomeFeature("U"))
        assertThat(BiomeFeature("X")).isEqualTo(BiomeFeature("X"))
        assertThat(BiomeFeature("X", Feature(ProduceResource(Oxygen)))).isNotEqualTo(BiomeFeature("X"))

    }

}