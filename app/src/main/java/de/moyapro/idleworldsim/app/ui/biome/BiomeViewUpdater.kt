package de.moyapro.idleworldsim.app.ui.biome

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import de.moyapro.idleworldsim.domain.Biome

abstract class BiomeViewUpdater<X : RecyclerView.ViewHolder, T : RecyclerView.Adapter<X>>(
    val biome: Biome,
    private val adapter: T
) {

    private var updatePending: Boolean = false

    fun update() {
        if (updatePending)
            return
        updatePending = true
        Handler(Looper.getMainLooper()).post {
            updatePending = false
            adapter.notifyDataSetChanged()
        }
    }

    abstract fun start()
    abstract fun stop()
}

