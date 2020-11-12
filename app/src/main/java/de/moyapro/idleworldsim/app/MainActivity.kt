package de.moyapro.idleworldsim.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.app.ui.biome.ResourceListFragment
import de.moyapro.idleworldsim.app.ui.biome.SpeciesListFragment
import de.moyapro.idleworldsim.app.ui.main.MainFragment
import de.moyapro.idleworldsim.app.ui.world.BiomeListFragment
import de.moyapro.idleworldsim.app.ui.world.WorldFragmentDirections
import de.moyapro.idleworldsim.domain.Biome
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.valueObjects.ResourceType


class MainActivity : AppCompatActivity(),
    ResourceListFragment.OnResourceInteractionListener,
    SpeciesListFragment.OnSpeciesInteractionListener,
    BiomeListFragment.OnBiomeInteractionListener {

    private val navController by lazy { findNavController(R.id.fragment) }

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
        if (biome === null)
            return
        val action = WorldFragmentDirections.actionWorldFragmentToBiomeFragment(biome.id.toString())
        navController.navigate(action)
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }

    fun onAddBiome(view: View) {
        val biome = Game.createBiome("a new one")
        val action = WorldFragmentDirections.actionWorldFragmentToBiomeFragment(biome.id.toString())
        navController.navigate(action)
    }
}
