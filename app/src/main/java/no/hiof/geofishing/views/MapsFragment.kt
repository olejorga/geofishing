package no.hiof.geofishing.views

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.provider.SettingsSlicesContract.KEY_LOCATION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>


    //private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    //private var lastKnownLocation: Location? = null

//    val permissionQuery = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissionGranted ->
//        if (permissionGranted) {
//
//        }
//        else {
//
//        }
//
//    }


//    private val permissionsRequired = arrayOf(
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    )

//    val locationPermissionRequest = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        when {
//            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                // Precise location access granted.
//            }
//            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                // Only approximate location access granted.
//            } else -> {
//            // No location access granted.
//        }
//        }
//    }



//
//    private val requestMultiplePermissions =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            permissions.entries.forEach {
//                val granted = it.value
//                val permission = it.key
//                if (!granted) {
//                    val neverAskAgain = !ActivityCompat.shouldShowRequestPermissionRationale(
//                        requireActivity(),
//                        permission
//                    )
//                    if (neverAskAgain) {
//                        //user click "never ask again"
//                    } else {
//                        //show explain dialog
//                    }
//                    return@registerForActivityResult
//                }
//            }
//        }

//    private fun checkAppPermission() {
//        permissionsRequired.forEach { permission ->
//            if (ContextCompat.checkSelfPermission(requireContext(), permission)
//                == PackageManager.PERMISSION_DENIED
//            ) {
//                requestMultiplePermissions.launch(permissionsRequired)
//                return
//            }
//        }
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        if (savedInstanceState != null) {
//            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
//            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
//        }
//
//    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        //val sydney = LatLng(-34.0, 151.0)
        //googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val hio = LatLng(59.12927227233991, 11.352814708532474)
        googleMap.addMarker(MarkerOptions().position(hio).title("Marker for Høgskolen i Østfold"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hio, 17.0f))
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        locationPermissionRequest.launch(arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION))

        //checkAppPermission()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

//    private fun getLocationPermission() {
//        /*
//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.
//         */
//        if (ContextCompat.checkSelfPermission(requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED) {
//            locationPermissionGranted = true
//        } else {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
//        }
//    }
}