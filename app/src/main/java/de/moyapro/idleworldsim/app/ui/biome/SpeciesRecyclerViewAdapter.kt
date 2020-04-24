package de.moyapro.idleworldsim.app.ui.biome


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.app.ui.biome.SpeciesFragment.OnSpeciesInteractionListener
import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.util.toShortDecimalStr
import kotlinx.android.synthetic.main.fragment_species.view.*

/**
 * [RecyclerView.Adapter] that can display a [Species] and makes a call to the
 * specified [OnSpeciesInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class SpeciesRecyclerViewAdapter(
    private val biome: Biome,
    private val mListener: OnSpeciesInteractionListener?
) : RecyclerView.Adapter<SpeciesRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val mUpdateHandler = BiomeViewUpdateHandler(biome, this)

    init {
        mOnClickListener = View.OnClickListener { v ->
            val species = v.tag as Species
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onSpeciesInteraction(species)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_species, parent, false)
        return ViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mUpdateHandler.startObservation()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mUpdateHandler.stopObservation()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val species = biome.getSpecies()[position]
        val population = biome.resources[species]
        holder.mIdView.text = species.name
        holder.mContentView.text = (population * 1E6).toShortDecimalStr()

        with(holder.mView) {
            tag = species
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = biome.getSpecies().size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
