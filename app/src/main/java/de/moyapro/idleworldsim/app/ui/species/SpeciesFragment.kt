package de.moyapro.idleworldsim.app.ui.species

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.databinding.SpeciesFragmentBinding
import de.moyapro.idleworldsim.domain.Species

class SpeciesFragment : Fragment() {

    companion object {
        fun newInstance() = SpeciesFragment()
    }

    private val viewModel by viewModels<SpeciesViewModel>()
    private val args: SpeciesFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel.species = Game.getSpecies(args.speciesName)
        val binding: SpeciesFragmentBinding =
                DataBindingUtil.inflate(inflater, R.layout.species_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        TODO("foobar")
//        return binding
    }
}
