package de.moyapro.idleworldsim.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.moyapro.idleworldsim.Game
import de.moyapro.idleworldsim.R
import de.moyapro.idleworldsim.app.ui.biome.ResourceFragment
import de.moyapro.idleworldsim.app.ui.biome.SpeciesFragment
import de.moyapro.idleworldsim.app.ui.main.MainFragment
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.consumption.Resource


class MainActivity : AppCompatActivity(), ResourceFragment.OnResourceInteractionListener, SpeciesFragment.OnSpeciesInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Game.init()
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResourceInteraction(resource: Resource?) {
        // TODO: implement the onListFragmentInteraction-Method for Resource class
    }

    override fun onSpeciesInteraction(species: Species?) {
        // TODO: implement the onListFragmentInteraction-Method for Species class
    }
}
