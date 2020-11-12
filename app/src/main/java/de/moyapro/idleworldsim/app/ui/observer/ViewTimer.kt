package de.moyapro.idleworldsim.app.ui.observer

import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ViewTimer<O : Any, X : RecyclerView.ViewHolder, T : RecyclerView.Adapter<X>>(
    observable: O,
    adapter: T,
    private val delay: Long,
    private val period: Long
) : ViewUpdater<O, X, T>(observable, adapter) {

    var timer: Timer? = null

    private val timerTask = UpdateTimerTask(this)

    class UpdateTimerTask<O : Any, X : RecyclerView.ViewHolder, T : RecyclerView.Adapter<X>>(val owner: ViewTimer<O, X, T>) :
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
