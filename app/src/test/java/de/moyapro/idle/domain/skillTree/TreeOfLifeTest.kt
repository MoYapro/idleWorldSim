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
        val evolved = Feature()
        val root = evolved
        val feature1 = Feature()
        val feature2 = Feature()
        assertThat(
            TreeOfLife()
                .root(root)
                .branchInto(feature1)
                .branchInto(feature2)
        ).isEqualTo(setOf(feature1, feature2))
    }

    @Test
    fun getEvolvableFeaturesForCurrentlyEvolvedFeatures2() {
        val evolved = Feature()
        val root = evolved
        val feature1 = Feature()
        val feature2 = Feature()
        assertThat(
            TreeOfLife()
                .root(root)
                .branchInto(feature1)
                .branchInto(feature2)
                .extendBranch(feature2)
        ).isEqualTo(setOf(feature1, feature2))
    }

}