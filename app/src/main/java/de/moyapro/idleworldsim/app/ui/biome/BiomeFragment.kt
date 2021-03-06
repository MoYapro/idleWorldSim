package de.moyapro.idleworldsim.app.ui.biome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import de.blox.graphview.GraphView
import de.blox.graphview.tree.BuchheimWalkerAlgorithm
import de.blox.graphview.tree.BuchheimWalkerConfiguration
import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.domain.Biome
import java.util.*


class BiomeFragment : Fragment() {

    //private val viewModel by viewModels<BiomeViewModel>()
    private val args: BiomeFragmentArgs by navArgs()
    lateinit var biome: Biome

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        biome = Game.getBiome(UUID.fromString(args.biome))!!
        val layout = inflater.inflate(R.layout.biome_fragment, container, false)
        val graphView = layout.findViewById<GraphView>(R.id.graph)
        graphView.adapter = FoodChainGraphAdapter(biome.getFoodChainGraph())

        // set the algorithm here
        val configuration: BuchheimWalkerConfiguration = BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(300)
            .setSubtreeSeparation(300)
            .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()
        graphView.setLayout(BuchheimWalkerAlgorithm(configuration))


        Log.i(this::class.simpleName, biome.foodChain.asDotNotation())

        return layout
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is SpeciesListFragment)
            childFragment.biome = biome
    }
}
