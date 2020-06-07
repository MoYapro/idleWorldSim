package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.consumption.DummyConsumer
import de.moyapro.idleworldsim.domain.consumption.DummyPorC
import de.moyapro.idleworldsim.domain.consumption.DummyProducer
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.ProduceResource
import de.moyapro.idleworldsim.domain.two.Biome
import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BiomeTwoTest {

    @Test
    fun settle() {
        val species: Species = DummyPorC("X")
        assertThat(Biome().settle(species, Population(3.0))[species]).isEqualTo(Population(3.0))
    }

    @Test
    fun settleMultiple() {
        val speciesX = DummyProducer("X")
        val speciesU = DummyConsumer("U")
        val biome = Biome()
            .settle(speciesX, Population(3.0))
            .settle(speciesU, Population(99.0))
        assertThat(biome[speciesX]).isEqualTo(Population(3.0))
        assertThat(biome[speciesU]).isEqualTo(Population(99.0))
    }

    @Test
    fun addBasicResources() {
        val biome = Biome().settle(DummyProducer("WATER").evolveTo(Feature( ProduceResource(Water, Level(3)))))
        assertThat(biome.provides()[Water]).isEqualTo(Resource(Water, 8.0))
    }

}