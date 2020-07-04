package de.moyapro.idleworldsim.app.ui.biome


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.app.ui.biome.ResourceFragment.OnResourceInteractionListener
import de.moyapro.idleworldsim.domain.two.Biome
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
import kotlinx.android.synthetic.main.fragment_resource.view.*

/**
 * [RecyclerView.Adapter] that can display a [ResourceType] and makes a call to the
 * specified [OnResourceInteractionListener].
 */
class ResourceRecyclerViewAdapter(
    val biome: Biome,
    private val mListener: OnResourceInteractionListener?
) : RecyclerView.Adapter<ResourceRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val mUpdateHandler = BiomeViewUpdateHandler(biome, this)

    init {
        mOnClickListener = View.OnClickListener { v ->
            val resource = v.tag as ResourceType
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onResourceInteraction(resource)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_resource, parent, false)
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
        val resource = ResourceType.values()[position]
//        val quantity = biome.resources[resource]
//        holder.mIdView.text = resource.displayName
//        holder.mContentView.text = quantity.amount.toShortDecimalStr()

        with(holder.mView) {
            tag = resource
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = ResourceType.values().size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
