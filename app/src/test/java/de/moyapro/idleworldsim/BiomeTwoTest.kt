package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.traits.ConsumerTrait
import de.moyapro.idleworldsim.domain.traits.Feature
import de.moyapro.idleworldsim.domain.traits.ProduceResource
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Minerals
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BiomeTwoTest {

    @Test
    fun settle() {
        val species: Species = Species("X")
        assertThat(Biome().settle(species, Population(3.0))[species]).isEqualTo(Population(3.0))
    }

    @Test
    fun settleMultiple() {
        val speciesX = Species("X")
        val speciesU = Species("U")
        val biome = Biome()
            .settle(speciesX, Population(3.0))
            .settle(speciesU, Population(99.0))
        assertThat(biome[speciesX]).isEqualTo(Population(3.0))
        assertThat(biome[speciesU]).isEqualTo(Population(99.0))
    }

    @Test
    fun addBasicResources() {
        val producer = Species(
            "WATER",
            listOf(Feature(ProduceResource(Water, Level(3))))
        )
        assertThat(Biome().settle(producer)).isNotNull
    }

    @Test
    fun addConsumer() {
    }

    @Test
    fun consumerConsumesProducer() {
        val soil = Species("soil", listOf(Feature(ProduceResource(Minerals))))
        val gras = Species("gras", listOf(Feature(ConsumerTrait(Minerals))))
        val biome = Biome()
            .settle(soil)
            .settle(gras)
            .process()
        val populationDifference: Map<Species, Population> = biome.getPopulationChanges()
        assertThat(populationDifference[soil]?.populationSize).isEqualTo(0.0)
        assertThat(populationDifference[gras]?.populationSize).isGreaterThan(0.0)


    }

}