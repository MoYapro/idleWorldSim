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
        val autotroph = Feature("Autotroph")
        val photosynthese = Feature("Photosynthese")
        val vertebrate = Feature("Spinal Cord")
        val plantEater = Feature("PlantEater")
        val meateater = Feature("Meateater")
        val smallPlant = Feature("Small Plant")
        val largePlant = Feature("Large Plant")
        val treeOfLife = TreeOfLife(autotroph)
        val plants = treeOfLife.branchInto(photosynthese)
        val animals = treeOfLife.branchInto(photosynthese)

        val grass = plants.branchInto(smallPlant)
        val tree = plants.branchInto(largePlant)
        val sheep = animals.branchInto(plantEater)
        val wolf = animals.branchInto(meateater)

        assertThat(treeOfLife.getEvolvableFeaures(autotroph)).isEqualTo(setOf(photosynthese, vertebrate))
        assertThat(treeOfLife.getEvolvableFeaures(vertebrate)).isEqualTo(setOf(sheep, wolf))
        assertThat(treeOfLife.getEvolvableFeaures(smallPlant)).isEqualTo(setOf<Feature>())
    }
}
