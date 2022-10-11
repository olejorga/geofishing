package no.hiof.geofishing.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    var mGoogleMap: GoogleMap? = null

    var locationPermissionGranted = false

    private val defLatitude = 59.12927227233991
    private val defLongitude = 11.352814708532474

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

//    private val locationPermissionsRequired = arrayOf(
//        Manifest.permission.ACCESS_COARSE_LOCATION,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    )
//
//    // TODO legg te else der du sett locationPermissionGranted = true
//    private fun checkLocationPermissions() {
//        locationPermissionsRequired.forEach { permission ->
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(), permission
//                ) == PackageManager.PERMISSION_DENIED
//            ) {
//                requestLocationPermissions.launch(locationPermissionsRequired)
//                return
//            }
//        }
//    }
//
//    private val requestLocationPermissions =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            permissions.entries.forEach {
//                val isGranted = it.value
//                val permission = it.key
//                when {
//                    isGranted -> locationPermissionGranted = true
//                    ActivityCompat.shouldShowRequestPermissionRationale(
//                        requireActivity(), permission
//                    ) -> {
//                        AlertDialog.Builder(requireContext())
//                            .setTitle(R.string.perm_request_rationale_title)
//                            .setMessage(R.string.perm_request_rationale)
//                            .setPositiveButton(R.string.request_perm_again) { _, _ ->
//                                checkLocationPermissions()
//                            }.setNegativeButton(R.string.dismiss, null).create().show()
//                    }
//                }
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            locationPermissionGranted = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        checkLocationPermissions()


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initializeMap()
    }

    private fun initializeMap() {
        val locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.interval = 5000
        locationRequest.priority =
            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.smallestDisplacement = 16.0f
        locationRequest.fastestInterval = 3000

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
//        if (!locationPermissionGranted) {
//            checkLocationPermissions()
//        } else
//            googleMap.isMyLocationEnabled = true
        mGoogleMap = googleMap

        //checkLocationPermissions()
        googleMap.isMyLocationEnabled = locationPermissionGranted
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            val latLng =
                LatLng(
                    location?.latitude ?: defLatitude,
                    location?.longitude ?: defLongitude
                )
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
        }


//        fusedLocationProviderClient.lastLocation.addOnFailureListener(OnFailureListener { })
//            .addOnSuccessListener(OnSuccessListener<Location>() {
//                val latLng = LatLng(it.latitude, it.longitude)
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
//            })
    }

    override fun onResume() {
        super.onResume()
        mGoogleMap?.clear()
    }
}

