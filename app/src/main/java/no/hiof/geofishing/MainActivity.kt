package no.hiof.geofishing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // Setup of navController since this is the navHost class
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Using the Navigation component(nav_graph, NavHostFragment, NavController)
        // Set the navHostFragment to the id of FragmentContainer(navHost) from activity_main.xml
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // navController for the navHostFragment
        navController = navHostFragment.navController

        // The setOf top-destination fragments of the app so the Up-button is not displayed when
        // navHost is at a top-level fragment
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mapsFragment,
                R.id.feedFragment,
                R.id.catchFragment,
                R.id.rankFragment,
                R.id.userPageFragment
            )
        )

        // Finish setup of toolbar with navController and config
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // setting up the binding and navController for bottomNavigation
        val navView: BottomNavigationView = binding.bottomNavigation
        navView.setupWithNavController(navController)
    }
}
