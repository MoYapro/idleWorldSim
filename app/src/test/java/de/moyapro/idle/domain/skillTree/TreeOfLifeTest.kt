package de.moyapro.idle.domain.skillTree

import de.moyapro.idle.domain.traits.Feature
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("UNUSED_VARIABLE", "LocalVariableName")
internal class TreeOfLifeTest {

    @Test
    fun createTreeOfLifeWithRootFeature() {
        assertThat(TreeOfLife(Feature())).isNotNull
    }

    @Test
    fun createTreeOfLifeWithSeveralFeatures() {
        val root = Feature()
        val feature1 = Feature()
        val feature2 = Feature()
        assertThat(
            TreeOfLife(root)
                .branchInto(feature1)
                .branchInto(feature2)
        ).isNotNull
    }

    @Test
    fun getEvolvableFeaturesForCurrentlyEvolvedFeatures1() {
        val autotrophic = Feature("Autotrophic")
        val photosynthesis = Feature("Photosynthesis")
        val vertebrate = Feature("Spinal Cord")
        val herbivore = Feature("Herbivore")
        val carnivore = Feature("Carnivore")
        val smallPlant = Feature("Small Plant")
        val largePlant = Feature("Large Plant")
        val root = TreeOfLife(autotrophic)
        val plants = root.branchInto(photosynthesis)
        val animals = root.branchInto(vertebrate)

        val grass = plants.branchInto(smallPlant)
        val tree = plants.branchInto(largePlant)
        val sheep = animals.branchInto(herbivore)
        val wolf = animals.branchInto(carnivore)

        assertThat(root.getEvolvableFeatures(autotrophic)).isEqualTo(setOf(photosynthesis, vertebrate))
        assertThat(root.getEvolvableFeatures(vertebrate)).isEqualTo(setOf(vertebrate, carnivore))
        assertThat(root.getEvolvableFeatures(photosynthesis)).isEqualTo(setOf(smallPlant, largePlant))
        assertThat(root.getEvolvableFeatures(smallPlant)).isEqualTo(setOf<Feature>())
    }
}
