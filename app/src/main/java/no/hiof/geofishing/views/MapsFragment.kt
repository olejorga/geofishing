package no.hiof.geofishing.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentMapsBinding

class MapsFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var mGoogleMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermissionGranted = false
    private var count = 0

    // def coordinates HIØ
    private val defLatitude = 59.12927227233991
    private val defLongitude = 11.352814708532474

    // Permissions required for location
    private val locationPermissionsRequired = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // TODO legg te else der du sett locationPermissionGranted = true
    // Checks if permissions are granted, if not starts a new req, else set user location.
    private fun checkLocationPermissions() {
        locationPermissionsRequired.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(), permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestLocationPermissions.launch(locationPermissionsRequired)
                return
            } else {
                setUserLocation(locationPermissionGranted)
            }
        }
    }

    private val requestLocationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value
                val permission = it.key
                when {
                    isGranted -> setUserLocation(it.value)
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), permission
                    ) -> { // TODO slett alert på dismiss og fiks aldri spør igjen logikk
//                        count++
//                        if (count < 1)
//                            return@forEach
                        AlertDialog.Builder(requireContext())
                            .setTitle(R.string.permission_request_rationale_title)
                            .setMessage(R.string.permission_request_rationale)
                            .setPositiveButton(R.string.request_permission_again) { _, _ ->
                                checkLocationPermissions()
                            }.setNegativeButton(R.string.dismiss_permission_dialog, null).create().show()
                    }
                }
            }
        }

    /**
     * @param permissionsEnabledBool A Boolean set through permission requests.
     *
     * */
    @SuppressLint("MissingPermission")
    private fun setUserLocation(permissionsEnabledBool: Boolean) {
        mGoogleMap?.isMyLocationEnabled = permissionsEnabledBool
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            val latLng =
                LatLng(
                    location?.latitude ?: defLatitude,
                    location?.longitude ?: defLongitude
                )
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gets the map async, callback = onMapReady
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        initializeMap()
    }

    // TODO add fab for adding catch der du sende latlng som input via action?
    // TODO easy permissions
    private fun initializeMap() {
//        val locationRequest = com.google.android.gms.location.LocationRequest()
//        locationRequest.interval = 5000
//        locationRequest.priority =
//            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.smallestDisplacement = 16.0f
//        locationRequest.fastestInterval = 3000

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        checkLocationPermissions()
    }

}

