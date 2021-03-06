package de.moyapro.idleworldsim.app.ui.biome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.blox.graphview.Graph
import de.blox.graphview.GraphAdapter
import de.blox.graphview.GraphView
import de.blox.graphview.Node
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.domain.Species

class FoodChainGraphAdapter(private val foodChainGraph: Graph) : GraphAdapter<GraphView.ViewHolder>(foodChainGraph) {

    override fun getCount(): Int {
        return foodChainGraph.nodes.size
    }

    override fun getItem(position: Int): Any {
        return foodChainGraph.nodes[position]
    }

    override fun isEmpty(): Boolean {
        return foodChainGraph.nodes.isEmpty()
    }

    override fun onBindViewHolder(viewHolder: GraphView.ViewHolder, data: Any, position: Int) {
        val text = ((getItem(position) as Node).data as Species).name
        (viewHolder as SimpleViewHolder).textView.text = text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.node_layout, parent, false);
        return SimpleViewHolder(view);
    }
}

internal class SimpleViewHolder(itemView: View) : GraphView.ViewHolder(itemView) {
    var textView: TextView = itemView.findViewById(R.id.nodeText)
}
