package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Population
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TraitTest {

    @Test
    fun createGrowthTrait() {
        assertThat(GrowthTrait).hasFieldOrPropertyWithValue("level", 1)
    }

    @Test
    fun increasedGrowthTrait() {
        val species = defaultSpecies()
        val resources = Resources()
        resources.setPopulation(species, Population(1.0))
        assertThat(
            species.process(resources).get(species).populationSize
        )
            .isLessThan(species.evolve(GrowthTrait).process(resources).get(species).populationSize)
    }

    @Test
    fun energySaver() {
        val species = defaultSpecies()
        val resources = Resources()
        resources.setPopulation(species, Population(1.0))
        assertThat(species.process(resources)[Energy]).isLessThan(species.evolve(EnergySaver).process(resources)[Energy])
    }

    @Test
    fun waterSaver() {
        val species = defaultSpecies()
        val resources = Resources()
        resources.setPopulation(species, Population(1.0))
        assertThat(species.process(resources)[Water]).isLessThan(species.evolve(WaterSaver).process(resources)[Water])
    }

    @Test
    fun mineralSaver() {
        val species = defaultSpecies()
        val resources = Resources()
        resources.setPopulation(species, Population(1.0))
        assertThat(species.process(resources)[Minerals]).isLessThan(
            species.evolve(MineralSaver).process(resources)[Minerals]
        )
    }

    @Test
    fun predatorsNeedWater() {
        val sheep = defaultSpecies("sheep").evolve(Meaty)
        val wolf = Species("Wolf")
            .evolve(Predator(Meaty), ConsumerTrait(Water))
        assertThat(
            Biome().settle(wolf).settle(sheep)
                .process().resources[wolf].populationSize
        ).`as`("Wolf needs water")
            .isGreaterThan(1.0)
    }

    @Test
    fun predatorsCanOnlyEatSomeSpecies() {
        // this test is failing sometimes. it may depend on the order in which the species are processed
        val gras = Species("Gras")
            .evolve(Feature.sunlightConsumer())
        val wolf = Species("Wolf").evolve(Predator(Meaty), NeedResource(Minerals), NeedResource(Energy))
        val biome = Biome()
            .settle(gras)
            .settle(wolf)
            .process()

        assertThat(biome.resources.get(gras).populationSize).`as`("Gras not eaten by wolf").isGreaterThan(1.0)
        assertThat(biome.resources.get(wolf).populationSize).`as`("Wolf cannot eat anything").isLessThan(1.0)
    }

    @Test
    fun foldTest() {
        val traits = listOf<Trait>()
        val value = 1
        assertThat(traits.fold(value) { v, _ -> v }).isEqualTo(value)
    }

    @Test
    fun lowDeathRate() {
        val lowDeathSpecies = defaultSpecies().evolve(LowDeathRate)
        val species = defaultSpecies()
        val biome = Biome(resources = Resources() * 0.0)
            .settle(lowDeathSpecies)
            .settle(species)
            .process()
        assertThat(biome.resources[species].populationSize).isGreaterThan(biome.resources[species].populationSize)
    }

}
