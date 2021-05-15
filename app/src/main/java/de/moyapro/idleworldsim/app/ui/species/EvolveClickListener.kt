package de.moyapro.idleworldsim.app.ui.species

import android.view.View
import android.widget.AdapterView
import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.traits.Feature

class EvolveClickListener(
    private val biome: Biome,
    private val species: Species
) : AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val featureToEvolve = parent.getItemAtPosition(position)
        biome.evolve(species, featureToEvolve as Feature)

    }
}
