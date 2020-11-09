package de.moyapro.idleworldsim.app.ui.main


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.app.ui.biome.ViewTimer
import de.moyapro.idleworldsim.app.ui.main.BiomeListFragment.OnBiomeInteractionListener
import de.moyapro.idleworldsim.domain.Biome
import kotlinx.android.synthetic.main.fragment_biome_list_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [Biome] and makes a call to the
 * specified [OnBiomeInteractionListener].
 */
class BiomeRecyclerViewAdapter(
    private val mListener: OnBiomeInteractionListener?
) : RecyclerView.Adapter<BiomeRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val mUpdateHandler = ViewTimer(Game, this, 0, 1000)

    init {
        mOnClickListener = View.OnClickListener { v ->
            val biome = v.tag as Biome
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onBiomeInteraction(biome)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_biome_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mUpdateHandler.start()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mUpdateHandler.stop()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val biomeList = Game.biomes()
        val biome = biomeList[position]
        holder.mIdView.text = biome.name
//        holder.mContentView.text = (population?.populationSize)?.toShortDecimalStr(1E6) ?: "dead"

        with(holder.mView) {
            tag = biome
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int {
        return Game.biomes().size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
