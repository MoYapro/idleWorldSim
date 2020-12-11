package de.moyapro.idleworldsim.app.ui.species

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.moyapro.idleworldsim.domain.traits.Feature


class FeatureListViewAdapter(
    private val features: List<Feature>,
    private val layoutInflater: LayoutInflater
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
        val listItem: View = convertView
            ?: layoutInflater.inflate(R.layout.activity_list_item, container, false)
        (listItem.findViewById(R.id.text1) as TextView).text = getItem(position).name
        return listItem
    }

    override fun getItem(position: Int) = features[position]
    override fun getItemId(position: Int) = getItem(position).hashCode().toLong()
    override fun getCount() = features.size
}
