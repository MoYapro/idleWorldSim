package de.moyapro.idleworldsim.app.ui.observer

import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ViewObserver<O : Any, X : RecyclerView.ViewHolder, T : RecyclerView.Adapter<X>>(
    observable: O,
    adapter: T
) : ViewUpdater<O, X, T>(observable, adapter), Observer {

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
