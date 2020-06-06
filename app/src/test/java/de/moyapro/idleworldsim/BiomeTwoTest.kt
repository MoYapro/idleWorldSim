package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.consumption.DummyConsumer
import de.moyapro.idleworldsim.domain.consumption.DummyPorC
import de.moyapro.idleworldsim.domain.consumption.DummyProducer
import de.moyapro.idleworldsim.domain.two.Biome
import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.domain.valueObjects.Population
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
        val biome = Biome().settle(DummyProducer("WATER"))
//        assertThat(biome.provides())
    }

}