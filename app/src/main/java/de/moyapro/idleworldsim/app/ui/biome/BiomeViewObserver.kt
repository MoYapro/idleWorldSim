package de.moyapro.idleworldsim.app.ui.biome

import androidx.recyclerview.widget.RecyclerView
import de.moyapro.idleworldsim.domain.Biome
import java.util.*

class BiomeViewObserver<X : RecyclerView.ViewHolder, T : RecyclerView.Adapter<X>>(
    biome: Biome,
    adapter: T
) : BiomeViewUpdater<X, T>(biome, adapter), Observer {

    override fun update(o: Observable?, arg: Any?) {
        update()
    }

    override fun start() {
//        biome.onBiomeProcess.addObserver(this)
    }

    override fun stop() {
//        biome.onBiomeProcess.deleteObserver(this)
    }
}
