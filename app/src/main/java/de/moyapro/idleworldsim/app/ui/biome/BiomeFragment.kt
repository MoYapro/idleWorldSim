package de.moyapro.idleworldsim.app.ui.biome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.domain.Biome
import java.util.*

class BiomeFragment : Fragment() {

    companion object {
        fun newInstance() = BiomeFragment()
    }

    //private val viewModel by viewModels<BiomeViewModel>()
    private val args: BiomeFragmentArgs by navArgs()
    lateinit var biome: Biome

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        biome = Game.getBiome(UUID.fromString(args.biome))!!
        return inflater.inflate(R.layout.biome_fragment, container, false)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is SpeciesListFragment)
            childFragment.biome = biome
    }
}
