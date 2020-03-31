package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.Species
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EvolutionConditionTest {
    @Test
    fun CanEvolveWithoutCondition() {
        assertThat(
            Species("canEvolve")
                .canEvolve(EnergySaver(), EvolutionConditions())
        )
            .isEqualTo(true)
    }

    @Test
    fun CanEvolveWithUnmatchedCondition() {
        assertThat(
            Species("canEvolve")
                .canEvolve(EnergySaver(), EvolutionConditions().add(EvolutionCondition(required = WaterSaver(), evolveTo = MineralSaver())))
        )
            .isEqualTo(true)
    }

    @Test
    fun CannotEvolveWithConditionNotTrue() {
        assertThat(
            Species("canNotEvolve")
                .canEvolve(EnergySaver(), EvolutionConditions().add(EvolutionCondition(required = WaterSaver(), evolveTo = EnergySaver())))
        )
            .isEqualTo(false)
    }

    @Test
    fun CanEvolveWithConditionTrue() {
        assertThat(
            Species("canEvolve")
                .evolve(WaterSaver())
                .canEvolve(EnergySaver(), EvolutionConditions().add(EvolutionCondition(required = WaterSaver(), evolveTo = EnergySaver())))
        )
            .isEqualTo(true)
    }
}
