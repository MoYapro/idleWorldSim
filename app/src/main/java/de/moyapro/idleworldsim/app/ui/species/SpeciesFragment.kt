package de.moyapro.idleworldsim.app.ui.species

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.databinding.SpeciesFragmentBinding

class SpeciesFragment : Fragment() {

    private val viewModel by viewModels<SpeciesViewModel>()
    private val args: SpeciesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.species = Game.getSpecies(args.speciesName)
        val binding: SpeciesFragmentBinding = createBinding(inflater, container)

        return binding.root
    }

    private fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SpeciesFragmentBinding {
        val binding: SpeciesFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.species_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val currentFeatures = viewModel.species.features
        val availableFeatures = Game.getEvolveOptions(viewModel.species)

        binding.root.findViewById<ListView>(R.id.features_list_view).adapter = FeatureListViewAdapter(currentFeatures, layoutInflater)
        binding.root.findViewById<ListView>(R.id.available_features_list_view).adapter = FeatureListViewAdapter(availableFeatures, layoutInflater)
        return binding
    }
}
