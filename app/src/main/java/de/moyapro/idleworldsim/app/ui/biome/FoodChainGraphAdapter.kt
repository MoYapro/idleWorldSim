package de.moyapro.idleworldsim.app.ui.biome

import android.R.attr
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.blox.graphview.GraphAdapter
import de.blox.graphview.GraphView
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.domain.consumption.FoodChain

class FoodChainGraphAdapter(private val foodChain: FoodChain) : GraphAdapter<GraphView.ViewHolder>(foodChain.generateGraph()) {

    override fun getCount(): Int {
        return graph.nodes.size
    }

    override fun getItem(position: Int): Any {
        return graph.nodes[position]
    }

    override fun isEmpty(): Boolean {
        return graph.nodes.isEmpty()
    }

    override fun onBindViewHolder(viewHolder: GraphView.ViewHolder, data: Any, position: Int) {
        (viewHolder as SimpleViewHolder).textView.text = attr.data.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphView.ViewHolder {
        TODO("Not yet implemented")
    }
}

internal class SimpleViewHolder(itemView: View) : GraphView.ViewHolder(itemView) {
    var textView: TextView = itemView.findViewById(R.id.nodeText)
}
