package de.moyapro.idle.domain.skillTree

import de.moyapro.idle.domain.traits.Feature
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("UNUSED_VARIABLE", "LocalVariableName")
internal class TreeOfLifeTest {

    @Test
    fun createEmptyTreeOfLife() {
        assertThat(TreeOfLife.build(Feature())).isNotNull
    }

    @Test
    fun createTreeOfLifeWithSeveralFeatures() {
        val root = Feature()
        val feature1 = Feature()
        val feature2 = Feature()
        assertThat(TreeOfLife.build(root) {
            branchInto(feature1)
            branchInto(feature2)
        }).isNotNull
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

        val tree = TreeOfLife.build(autotrophic) {
            branchInto(photosynthesis) {
                branchInto(smallPlant)
                branchInto(largePlant)
            }
            branchInto(vertebrate) {
                branchInto(herbivore)
                branchInto(carnivore)
            }
        }

        assertThat(tree.getEvolvableFeatures(autotrophic)).isEqualTo(setOf(photosynthesis, vertebrate))
        assertThat(tree.getEvolvableFeatures(vertebrate)).isEqualTo(setOf(herbivore, carnivore))
        assertThat(tree.getEvolvableFeatures(photosynthesis)).isEqualTo(setOf(smallPlant, largePlant))
        assertThat(tree.getEvolvableFeatures(smallPlant)).isEqualTo(setOf<Feature>())
    }

    @Test
    fun featuresHaveOrder() {

    }

}
