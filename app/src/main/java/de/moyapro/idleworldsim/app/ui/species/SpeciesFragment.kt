package de.moyapro.idleworldsim.app.ui.species

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.moyapro.idleworldsim.R

class SpeciesFragment : Fragment() {

    companion object {
        fun newInstance() = SpeciesFragment()
    }

    //private val viewModel by viewModels<SpeciesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.species_fragment, container, false)
    }
}
