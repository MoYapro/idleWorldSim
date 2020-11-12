package de.moyapro.idleworldsim.app.ui.world

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.moyapro.idleworldsim.R

class WorldFragment : Fragment() {

    companion object {
        fun newInstance() = WorldFragment()
    }

    //private val viewModel by viewModels<WorldViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.world_fragment, container, false)
    }

}
