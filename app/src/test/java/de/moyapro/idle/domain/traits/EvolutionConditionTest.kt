package de.moyapro.idle.domain.traits

import de.moyapro.idle.domain.Species
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EvolutionConditionTest {
    @Test
    fun canEvolveWithoutCondition() {
        assertThat(
            Species("canEvolve")
                .canEvolve(EnergySaver(), EvolutionConditions())
        )
            .isEqualTo(true)
    }

    @Test
    fun canEvolveWithUnmatchedCondition() {
        assertThat(
            Species("canEvolve")
                .canEvolve(EnergySaver(), EvolutionConditions().add(EvolutionCondition(required = WaterSaver(), evolveTo = MineralSaver())))
        )
            .isEqualTo(true)
    }

    @Test
    fun cannotEvolveWithConditionNotTrue() {
        assertThat(
            Species("canNotEvolve")
                .canEvolve(EnergySaver(), EvolutionConditions().add(EvolutionCondition(required = WaterSaver(), evolveTo = EnergySaver())))
        )
            .isEqualTo(false)
    }

    @Test
    fun canEvolveWithConditionTrue() {
        assertThat(
            Species("canEvolve")
                .evolve(WaterSaver())
                .canEvolve(EnergySaver(), EvolutionConditions().add(EvolutionCondition(required = WaterSaver(), evolveTo = EnergySaver())))
        )
            .isEqualTo(true)
    }
}
