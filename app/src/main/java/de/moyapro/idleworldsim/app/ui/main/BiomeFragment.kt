package de.moyapro.idleworldsim.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.moyapro.idleworldsim.R

class BiomeFragment : Fragment() {

    companion object {
        fun newInstance() = BiomeFragment()
    }

    //private val viewModel by viewModels<BiomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.biome_fragment, container, false)
    }
}
