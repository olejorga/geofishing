package no.hiof.geofishing.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import no.hiof.geofishing.BuildConfig.MAPS_API_KEY
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentMapsBinding

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var mGoogleMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // default coordinates HIØ
    private val defLatitude = 59.12927227233991
    private val defLongitude = 11.352814708532474

//    private lateinit var placesClient: PlacesClient

    // Permissions required for location
    private val locationPermissionsRequired = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

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
                setUserLocation()
            }
        }
    }

// TODO hvis bruker velger coarse_loc og deny, for så å klikke back og velge coarse og accept så vil AlertDialog poppe opp for Fine_loc

    /**
     * Requests user to choose between either coarse or fine location, if dismissed shows an
     * AlertDialog of why the app needs these permissions depending on the
     * shouldShowRequestPermissionRationale method.
     * if check on array to prevent double AlertDialog, if fine_location is granted so is coarse
     * if only coarse setUserLocation, if none, only build AlertDialog on fine_location
     */
    private val requestLocationPermissions =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value
                val permission = it.key
                when {
                    isGranted -> setUserLocation()
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), permission
                    ) -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle(R.string.permission_request_title)
                            .setMessage(R.string.permission_request_rationale)
                            .setPositiveButton(R.string.request_permission_again) { _, _ ->
                                checkLocationPermissions()
                            }.setNegativeButton(R.string.deny_permission_dialog, null).create()
                            .show()
                    }
                }
            }
        }


    @SuppressLint("MissingPermission")
    private fun setUserLocation() {
        mGoogleMap?.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            val latLng =
                LatLng(
                    location?.latitude ?: defLatitude,
                    location?.longitude ?: defLongitude
                )
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
        }


//        // Use fields to define the data types to return.
//        val placeFields: List<Place.Field> = listOf(Place.Field.NAME)
//
//        // Use the builder to create a FindCurrentPlaceRequest.
//        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)
//
//        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
//
//        val placeResponse = placesClient.findCurrentPlace(request)
//        placeResponse.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val response = task.result
//                for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods
//                    ?: emptyList()) {
//                    Log.d(
//                        "lol1",
//                        "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}"
//                    )
//                }
//            } else {
//                val exception = task.exception
//                if (exception is ApiException) {
//                    Log.d("lol2", "Place not found: ${exception.statusCode}")
//                }
//            }
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Construct a PlacesClient
//        Places.initialize(requireContext(), MAPS_API_KEY)
//        placesClient = Places.createClient(requireContext())

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gets the map async, callback = onMapReady
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        checkLocationPermissions()
    }

}

