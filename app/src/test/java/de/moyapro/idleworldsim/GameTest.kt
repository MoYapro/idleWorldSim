package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.traits.Feature
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameTest {
    @Test
    fun speciesHasEvolveOptions() {
        Game.selectedSpecies = Game.selectedBiome.species().first()
        val evolveOptions: List<Feature> = Game.getEvolveOptions()
        assertThat(evolveOptions).isNotEmpty
    }
}