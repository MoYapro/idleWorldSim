package de.moyapro.idleworldsim.app.ui.biome

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import de.moyapro.idleworldsim.domain.Biome
import java.util.*

class BiomeViewUpdateHandler<X : RecyclerView.ViewHolder, T : RecyclerView.Adapter<X>>(
    val biome: Biome,
    private val adapter: T
) : Observer {

    private var updatePending: Boolean = false

    override fun update(o: Observable?, arg: Any?) {
        if (updatePending)
            return
        updatePending = true
        Handler(Looper.getMainLooper()).post {
            updatePending = false
            adapter.notifyDataSetChanged()
        }
    }

    fun startObservation() {
        biome.onBiomeProcess.addObserver(this)
    }

    fun stopObservation() {
        biome.onBiomeProcess.deleteObserver(this)
    }
}
