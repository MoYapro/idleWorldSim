package com.example.idleworldsim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.idleworldsim.ui.main.MainFragment
import de.moyapro.idleworldsim.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}
