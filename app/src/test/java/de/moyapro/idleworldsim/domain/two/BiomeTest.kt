package de.moyapro.idleworldsim.domain.two

import de.moyapro.idleworldsim.domain.consumption.*
import de.moyapro.idleworldsim.domain.traits.*
import de.moyapro.idleworldsim.domain.valueObjects.Resource
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BiomeTest {
    @Test
    fun createBiome() {
        assertNotNull(Biome(), "Should create biome instance")
    }

    @Test
    fun defaultBiomeIsStable() {
        assertEquals(Biome(), Biome().process())
    }

    @Test
    fun addStaticResourceProdcuerToBiome() {
        Biome()
            .addResourceProducer(DummyProducer("Water").evolveTo(Feature(ProduceResource(Water))))
            .process()
    }

    @Test
    fun biomeResourceUpdateAfterGenerating() {
        val biome = Biome().settle(defaultSpecies()).process()
        assertNotEquals(
            0,
            biome.resources[EvolutionPoints],
            "Species should change resources in biome"
        )
    }

    @Test
    fun speciesGrowAndDie() {
        val species = defaultSpecies()
        val biome = Biome().settle(species)
        val initialSpeciesSize = biome[species]
        biome.process()
        assertNotEquals(
            initialSpeciesSize,
            biome[species],
            "Should update speciesSize when generating in biome"
        )
    }

    @Test
    fun speciesConsumeWater() {
        val initialWaterLevel = Resource(Water, 100_000.0)
        val biome = Biome(resources = Resources().setQuantity(initialWaterLevel))
            .settle(defaultSpecies())
            .process()
        assertThat(biome.resources[Water].amount).isLessThan(initialWaterLevel.amount)

    }

    @Test
    fun speciesShouldShrinkOnResourceShortage() {
        val species1 = Species("I").evolve(NeedResource(Water), ConsumerTrait(Water))
        val species2 = Species("II").evolve(NeedResource(Water), ConsumerTrait(Water))
        val usualGrowthResult = Biome(resources = Resources(Water, 1000.0)).settle(species1).process().resources[species1]
        val cappedGrowthResult = Biome(resources = emptyResources()).settle(species2).process().resources[species2]
        assertThat(cappedGrowthResult.populationSize).isLessThan(usualGrowthResult.populationSize)
    }

    @Test
    fun speciesShouldNotConsumeOnResourceShortage() {
        val initialResources = Resources(values().map { Resource(it, 0.0) })
        val resourcesAfterGeneration =
            Biome(resources = initialResources)
                .settle(defaultSpecies())
                .process()
                .resources
        assertThat(initialResources.quantities.entries).containsExactlyInAnyOrder(*resourcesAfterGeneration.quantities.entries.toTypedArray())
    }

    @Test
    fun biomeStatusText() {
        val biomeName = "DefaultBiome${Math.random()}"
        val expectedBiomeStatus = """
            BiomeStatus: $biomeName
            Resources(Minerals=999.0, Oxygen=1000.0, EvolutionPoints=1.0, Energy=999.0, Water=999.0)[[Species[Species2 | grow=GrowthRate(rate=1.1), die=DeathRate(rate=1.0)]:1.0, Species[Species1 | grow=GrowthRate(rate=1.1), die=DeathRate(rate=1.0)]:1.1]]
            Species1: 1.1M -> 1.21M
            Species2: 1.0M -> 1.1M
            """.trimIndent()
        val biome = Biome(biomeName, Resources()).settle(defaultSpecies(name = "Species1")).process()
            .settle(defaultSpecies("Species2"))
        assertThat(biome.getStatusText()).isEqualTo(expectedBiomeStatus)
    }

    @Test
    fun speciesCanEatEachOther() {
        val initialResources = Resources(DoubleArray(values().size) { 2.0 })
        val predator = defaultSpecies("Predator")
        predator.evolve(Predator(Meaty))
        val prey = defaultSpecies("Prey").evolve(Meaty)
        val biome = Biome(resources = initialResources)
            .settle(predator)
            .settle(prey)
            .process()
        assertThat(predator.getPopulationIn(biome).populationSize).isGreaterThan(1.0)
        assertThat(prey.getPopulationIn(biome).populationSize).isLessThan(1.1)
    }

    @Test
    fun speciesEatsAnotherSpecies() {
        val predator = defaultSpecies("Eater").evolve(Predator(Meaty))
        val prey = defaultSpecies("Food").evolve(Meaty)
        val uninvolved = defaultSpecies("Uninvolved")
        val biome = Biome("Earth", Resources())
            .settle(predator)
            .settle(prey)
            .settle(uninvolved)
            .process()
        assertThat(predator.getPopulationIn(biome).populationSize).isGreaterThan(1.0)
        assertThat(prey.getPopulationIn(biome).populationSize).isLessThan(uninvolved.getPopulationIn(biome).populationSize)
    }

    @Test
    fun biomeGeneratesResources() {
        val empty = Resources()
        val generation = Resources(DoubleArray(values().size) { 1.0 })
        val biome = Biome(resources = empty, generation = generation).process()
        assertThat(biome.resources[Minerals].amount).isGreaterThan(0.0)
    }

    @Test
    fun biomeWithEqualGenerationAndConsumptionIsStable() {
        val initial = Resources()
        val generation = Resources(DoubleArray(values().size) { 1.0 })
        val species = defaultSpecies()
        val biome = Biome(resources = initial, generation = generation).settle(species).process()
        val expectedResources = Resources(DoubleArray(values().size) { if (it == EvolutionPoints.ordinal) 2.0 else 1000.0 }) // 1EP from Biome and 1EP from Species
        expectedResources[Oxygen] = Resource(Oxygen, 1001.0)
        assertThat(biome.resources.quantities.entries).containsExactlyInAnyOrder(*expectedResources.quantities.entries.toTypedArray())
    }

    @Test
    fun biomeStoresResourcesGeneratedBySpecies() {
        val empty = Resources()
        val generation = Resources(DoubleArray(values().size) { 1.0 })
        generation[Oxygen] = Resource(Oxygen, 0.0) // biome does not create oxygen
        val species = defaultSpecies().evolve(Feature.sunlightConsumer())
        val biome = Biome(resources = empty, generation = generation).settle(species).process()
        assertThat(biome.resources[Oxygen].amount).`as`("Oxygen is generated by species with trait OxygenProducer").isGreaterThan(0.0)
    }
}

fun defaultSpecies(name: String = "DefaultSpecies${Math.random()}"): Species {
    return SpeciesImpl(
        name, Feature(
            ConsumerTrait(Water),
            ConsumerTrait(Minerals),
            ConsumerTrait(Energy),
            NeedResource(Water),
            NeedResource(Minerals),
            NeedResource(Energy),
            ProduceResource(EvolutionPoints)
        )
    )
}
