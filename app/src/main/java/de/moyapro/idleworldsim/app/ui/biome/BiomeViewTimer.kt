package de.moyapro.idleworldsim.app.ui.biome

import androidx.recyclerview.widget.RecyclerView
import de.moyapro.idleworldsim.domain.Biome
import java.util.*

class BiomeViewTimer<X : RecyclerView.ViewHolder, T : RecyclerView.Adapter<X>>(
    biome: Biome,
    adapter: T,
    private val delay: Long,
    private val period: Long
) : BiomeViewUpdater<X, T>(biome, adapter) {

    var timer: Timer? = null

    private val timerTask = UpdateTimerTask(this)

    class UpdateTimerTask<X : RecyclerView.ViewHolder, T : RecyclerView.Adapter<X>>(val owner: BiomeViewTimer<X, T>) :
        TimerTask() {
        override fun run() {
            owner.update()
        }
    }

    override fun start() {
        timer = (timer ?: Timer()).apply {
            scheduleAtFixedRate(timerTask, delay, period)
        }
    }

    override fun stop() {
        timer = timer?.let {
            it.cancel()
            null
        }
    }

}
