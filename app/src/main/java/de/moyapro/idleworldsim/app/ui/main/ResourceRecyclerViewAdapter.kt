package de.moyapro.idleworldsim.app.ui.main


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.app.ui.biome.ViewTimer
import de.moyapro.idleworldsim.app.ui.main.ResourceListFragment.OnResourceInteractionListener
import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType
import de.moyapro.idleworldsim.util.toShortDecimalStr
import kotlinx.android.synthetic.main.fragment_resource_list_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [ResourceType] and makes a call to the
 * specified [OnResourceInteractionListener].
 */
class ResourceRecyclerViewAdapter(
    val biome: Biome,
    private val mListener: OnResourceInteractionListener?
) : RecyclerView.Adapter<ResourceRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val mUpdateHandler = ViewTimer(biome, this, 0, 1000)

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
            .inflate(R.layout.fragment_resource_list_item, parent, false)
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
        val resource = ResourceType.values()[position]
        val amount = 0.0 // biome.resources[resource].amount
        holder.mIdView.text = resource.displayName
        holder.mContentView.text = amount.toShortDecimalStr()

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
