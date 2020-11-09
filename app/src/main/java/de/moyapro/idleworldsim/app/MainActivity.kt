package de.moyapro.idleworldsim.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.app.ui.main.BiomeListFragment
import de.moyapro.idleworldsim.app.ui.main.MainFragment
import de.moyapro.idleworldsim.app.ui.main.ResourceListFragment
import de.moyapro.idleworldsim.app.ui.main.SpeciesListFragment
import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType


class MainActivity : AppCompatActivity(),
    ResourceListFragment.OnResourceInteractionListener,
    SpeciesListFragment.OnSpeciesInteractionListener,
    BiomeListFragment.OnBiomeInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResourceInteraction(resource: ResourceType?) {
        // TODO: implement the onListFragmentInteraction-Method for Resource class
    }

    override fun onSpeciesInteraction(species: Species?) {
        // TODO: implement the onListFragmentInteraction-Method for Species class
    }

    override fun onBiomeInteraction(biome: Biome?) {
        val navController = findNavController(R.id.fragment)
        navController.navigate(R.id.action_worldFragment_to_biomeFragment)
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.fragment)
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}
