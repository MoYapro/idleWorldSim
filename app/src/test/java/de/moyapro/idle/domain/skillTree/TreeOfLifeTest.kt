package de.moyapro.idle.domain.skillTree

import de.moyapro.idle.domain.traits.Feature
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TreeOfLifeTest {

    @Test
    fun createEmptyTreeOfLife() {
        assertThat(TreeOfLife()).isNotNull
    }

    @Test
    fun createTreeOfLifeWithRootFeature() {
        assertThat(TreeOfLife().root(Feature())).isNotNull
    }

    @Test
    fun createTreeOfLifeWithSeveralFeatures() {
        val root = Feature()
        val feature1 = Feature()
        val feature2 = Feature()
        assertThat(
            TreeOfLife()
                .root(root)
                .branchInto(feature1)
                .branchInto(feature2)
        ).isNotNull
    }

    @Test
    fun getEvolvableFeaturesForCurrentlyEvolvedFeatures1() {
        val evolvedRoot = Feature()
        val feature1 = Feature()
        val feature2 = Feature()
        val feature3 = Feature()
        val feature4 = Feature()
        val feature5 = Feature()
        val feature6 = Feature()
        val treeOfLife: TreeOfLife = TreeOfLife().root(evolvedRoot)
        val branch1 = treeOfLife.branchInto(feature1)
        val branch2 = treeOfLife.branchInto(feature2)

        val branch1_1 = branch1.branchInfo(feature3)
        val branch1_2 = branch1.branchInfo(feature4)
        val branch2_1 = branch2.branchInfo(feature5)
        val branch2_2 = branch2.branchInfo(feature6)

        assertThat(treeOfLife.getEvolvableFeaures(evolvedRoot)).isEqualTo(setOf(feature1, feature2))
        assertThat(treeOfLife.getEvolvableFeaures(feature2)).isEqualTo(setOf(feature5, feature6))
        assertThat(treeOfLife.getEvolvableFeaures(feature4)).isEqualTo(setOf())
    }
}