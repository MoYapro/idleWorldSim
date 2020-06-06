package de.moyapro.idleworldsim

import de.moyapro.idleworldsim.domain.two.Biome
import de.moyapro.idleworldsim.domain.two.Species
import de.moyapro.idleworldsim.domain.valueObjects.Population
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class BiomeTwoTest {

    @Test
    fun settle() {
        val species = Species("X")
        assertThat(Biome().settle(species, Population(3.0))[species]).isEqualTo(Population(3.0))
    }

    @Test
    fun process() {
        fail("Implement me")
    }

}