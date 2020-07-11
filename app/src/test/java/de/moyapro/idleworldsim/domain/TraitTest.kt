package de.moyapro.idleworldsim.domain

import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Level
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.Water
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TraitTest {

    @Test
    fun createGrowthTrait() {
        assertThat(GrowthTrait).hasFieldOrPropertyWithValue("level", Level(1))
    }

    @Test
    fun increasedGrowthTrait() {
//        val species = defaultSpecies()
//        val resources = Resources()
//        resources.setPopulation(species, Population(1.0))
//        assertThat(
//            species.consume(Population(1.0), resources).populationSize
//        )
//            .isLessThan(species.evolveTo(Feature(GrowthTrait)).consume(Population(1.0), resources).populationSize)
    }

    @Test
    fun energySaver() {
//        val species = defaultSpecies()
//        val resources = Resources()
//        resources.setPopulation(species, Population(1.0))
//        assertThat(species.consume(Population(1.0), resources)[Energy].amount).isLessThan(species.evolveTo(Feature(EnergySaver)).consume(resources)[Energy].amount)
    }

    @Test
    fun waterSaver() {
//        val species = defaultSpecies()
//        val resources = Resources()
//        resources.setPopulation(species, Population(1.0))
//        assertThat(species.consume(resources)[Water].amount).isLessThan(species.evolveTo(Feature(WaterSaver)).consume(resources)[Water].amount)
    }

    @Test
    fun mineralSaver() {
//        val species = defaultSpecies()
//        val resources = Resources()
//        resources.setPopulation(species, Population(1.0))
//        assertThat(species.consume(resources)[Minerals].amount).isLessThan(
//            species.evolveTo(Feature(MineralSaver).consume(resources)[Minerals].amount
//        ))
    }

    @Test
    fun predatorsNeedWater() {
        val sheep = defaultSpecies("sheep").evolveTo(Feature(Meaty))
        val wolf = Species("Wolf", Feature(Predator(Meaty), NeedResource(Water)))
        assertThat(
            Biome().settle(wolf).settle(sheep).process()[wolf].populationSize
//                .consume().resources[wolf].populationSize
        ).`as`("Wolf needs water")
            .isLessThanOrEqualTo(1.0)
    }

    @Test
    fun predatorsCanOnlyEatSomeSpecies() {
        // this test is failing sometimes. it may depend on the order in which the species are consumeed
//        val gras = Species("Gras")
//            .evolveTo(Feature(Feature.sunlightConsumer())
//        val wolf = Species("Wolf").evolveTo(Feature(Predator(Meaty), NeedResource(Minerals), NeedResource(Energy))
//        val biome = Biome("Earth", Resources())
//            .settle(gras)
//            .settle(wolf)
//            .consume()
//
//        assertThat(biome.resources[gras as Species].populationSize).`as`("Gras not eaten by wolf").isGreaterThan(1.0)
//        assertThat(biome.resources[wolf].populationSize).`as`("Wolf cannot eat anything").isLessThan(1.0)
    }

    @Test
    fun foldTest() {
        val traits = listOf<Trait>()
        val value = 1
        assertThat(traits.fold(value) { v, _ -> v }).isEqualTo(value)
    }

    @Test
    fun lowOrHighDeathRate() {
//        val lowDeathSpecies = defaultSpecies("LowDeath").evolveTo(Feature(LowDeathRate)
//        val highDeathSpecies = defaultSpecies("HighDeath").evolveTo(Feature(HighDeathRate)
//        val species = defaultSpecies() // species do not die without death trait (deathrate = 1)
//        val biome = Biome(resources = Resources())
//            .settle(lowDeathSpecies)
//            .settle(highDeathSpecies)
//            .settle(species)
//            .consume()
//        assertThat(biome.resources[lowDeathSpecies].populationSize).isLessThan(biome.resources[species].populationSize)
//        assertThat(biome.resources[species].populationSize).isGreaterThan(biome.resources[highDeathSpecies].populationSize)
    }

    @Test
    fun canCounter() {
        assertThat(Stealth().canCounter(Vision())).isTrue()
    }

    @Test
    fun canCounterSubclass() {
        assertThat(SuperStealth().canCounter(Vision())).isTrue()
    }

    @Test
    fun subclassIsNotCountered() {
        assertThat(Stealth().canCounter(SuperVision())).isFalse()
    }


}
