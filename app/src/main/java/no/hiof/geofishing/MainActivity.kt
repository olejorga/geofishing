package no.hiof.geofishing

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView

import no.hiof.geofishing.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var locationPermissionGranted: Boolean = false
    private lateinit var binding: ActivityMainBinding
    // Setup of navController since this is the navHost class
    private lateinit var navController: NavController

    private val locationPermissionsRequired = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // TODO legg te else der du sett locationPermissionGranted = true
    private fun checkLocationPermissions() {
        locationPermissionsRequired.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestLocationPermissions.launch(locationPermissionsRequired)
                return
            }
        }
    }

    private val requestLocationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value
                val permission = it.key
                when {
                    isGranted ->  locationPermissionGranted = true
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this, permission
                    ) -> {
                        AlertDialog.Builder(this)
                            .setTitle(R.string.perm_request_rationale_title)
                            .setMessage(R.string.perm_request_rationale)
                            .setPositiveButton(R.string.request_perm_again) { _, _ ->
                                checkLocationPermissions()
                            }.setNegativeButton(R.string.dismiss, null).create().show()
                    }
                }
            }
        }

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
                R.id.menu_maps_fragment,
                R.id.feedFragment,
                R.id.menu_catch_fragment,
                R.id.menu_rank_fragment,
                R.id.userPageFragment
            )
        )

        // Finish setup of toolbar with navController and config
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // setting up the binding and navController for bottomNavigation
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, view, _ ->
            if (view.id == R.id.loginFragment || view.id == R.id.signupFragment) {
                binding.bottomNavigation.visibility = View.GONE
                binding.toolbar.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.toolbar.visibility = View.VISIBLE
            }
        }
        checkLocationPermissions()
        //Log.d("HERE", "RUN REPO!")
    }
}
