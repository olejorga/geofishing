package no.hiof.geofishing.ui.views

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentMapsBinding
import no.hiof.geofishing.ui.utils.VectorToBitmapDescriptor.bitmapDescriptorFromVector
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.MapViewModel

class MapsFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels {
        ViewModelFactory.create {
            MapViewModel(
                (activity?.application as App).catchRepository
            )
        }
    }
    private var mGoogleMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // default coordinates HIØ
//    private val defLatitude = 59.12927227233991
//    private val defLongitude = 11.352814708532474

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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


    private val COORDINATE_OFFSET = 0.000085f
    private val markerCoordinates: ArrayList<LatLng> = ArrayList()
    private var offsetType = 0

    private fun getLatLng(latLng: LatLng): LatLng {
        var updateLatLng: LatLng

        if (markerCoordinates.contains(latLng)) {
            var latitude = 0.0
            var longitude = 0.0
            when (offsetType) {
                0 -> {
                    latitude = latLng.latitude + COORDINATE_OFFSET
                    longitude = latLng.longitude
                }
                1 -> {
                    latitude = latLng.latitude + COORDINATE_OFFSET
                    longitude = latLng.longitude
                }
                2 -> {
                    latitude = latLng.latitude
                    longitude = latLng.longitude + COORDINATE_OFFSET
                }
                3 -> {
                    latitude = latLng.latitude
                    longitude = latLng.longitude + COORDINATE_OFFSET
                }
                4 -> {
                    latitude = latLng.latitude + COORDINATE_OFFSET
                    longitude = latLng.longitude + COORDINATE_OFFSET
                }
            }
            offsetType++
            if (offsetType == 5)
                offsetType = 0

            updateLatLng = getLatLng(LatLng(latitude, longitude))

        } else {
            markerCoordinates.add(latLng)
            updateLatLng = latLng
        }
        return updateLatLng

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        checkLocationPermissions()

        viewModel.catchList.observe(viewLifecycleOwner) { response ->
            if (response.error == null && response.data != null) {
                val catchList = response.data
//                var counter = 0

                // TODO Mulig itereringa bli feil mot lista i feed??
                catchList.forEachIndexed { index, catch ->
                    if (catch.latitude != null && catch.longitude != null) {
                        val catchLatLng = getLatLng(LatLng(catch.latitude, catch.longitude))
                        var markerColor: BitmapDescriptor? = null
                        when (catch.species) {
                            resources.getStringArray(R.array.fish_array)[0] -> markerColor =
                                bitmapDescriptorFromVector(
                                    requireContext(),
                                    R.drawable.reshot_icon_blue_fish
                                )
                            resources.getStringArray(R.array.fish_array)[1] -> markerColor =
                                bitmapDescriptorFromVector(
                                    requireContext(),
                                    R.drawable.reshot_icon_bright_fish
                                )
                            resources.getStringArray(R.array.fish_array)[2] -> markerColor =
                                bitmapDescriptorFromVector(
                                    requireContext(),
                                    R.drawable.reshot_icon_brown_fish
                                )
                            resources.getStringArray(R.array.fish_array)[3] -> markerColor =
                                bitmapDescriptorFromVector(
                                    requireContext(),
                                    R.drawable.reshot_icon_cartoon_fish
                                )
                            resources.getStringArray(R.array.fish_array)[4] -> markerColor =
                                bitmapDescriptorFromVector(
                                    requireContext(),
                                    R.drawable.reshot_icon_cyan_fish
                                )
                            resources.getStringArray(R.array.fish_array)[5] -> markerColor =
                                bitmapDescriptorFromVector(
                                    requireContext(),
                                    R.drawable.reshot_icon_gold_fish
                                )
                            resources.getStringArray(R.array.fish_array)[6] -> markerColor =
                                bitmapDescriptorFromVector(
                                    requireContext(),
                                    R.drawable.reshot_icon_green_fish
                                )
                            resources.getStringArray(R.array.fish_array)[7] -> markerColor =
                                bitmapDescriptorFromVector(
                                    requireContext(),
                                    R.drawable.reshot_icon_leaf_fish
                                )
                        }

                        val marker: Marker? =
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(catchLatLng)
                                    .title(catch.title)
                                    .icon(markerColor)
                            )

                        marker?.tag = index
                    }
                }
            }
        }
        googleMap.setOnMarkerClickListener(this)
    }

    // passing along marker.tag to deeplink for arg in FeedPostDetailFragment.kt
    override fun onMarkerClick(marker: Marker): Boolean {
        val markerCatchId = marker.tag.toString()
        val uri = Uri.parse("my-app://Feed/${markerCatchId}")
        if (findNavController().graph.hasDeepLink(uri)) {
            Log.d("DEEPLINK EXIST", findNavController().graph.hasDeepLink(uri).toString())
            findNavController().navigate(uri)
        } else {
            Log.d("DEEPLINK EXISTN'T", findNavController().graph.hasDeepLink(uri).toString())
        }
        return false
    }

    // Checks if permissions are granted, if not starts a new req, else set user location.
    private fun checkLocationPermissions() {
        viewModel.locationPermissionsRequired.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(), permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestLocationPermissions.launch(viewModel.locationPermissionsRequired)
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
                            }.setNegativeButton(R.string.deny_permission_dialog, null)
                            .create()
                            .show()
                    }
                }
            }
        }

    @SuppressLint("MissingPermission")
    private fun setUserLocation() {
        mGoogleMap?.isMyLocationEnabled = true
        fusedLocationProviderClient.getCurrentLocation(
            100, null
        ).addOnSuccessListener { location: Location? ->
            val currentLocationLatLng =
                LatLng(
                    location?.latitude ?: viewModel.defLatitude,
                    location?.longitude ?: viewModel.defLongitude
                )
            mGoogleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentLocationLatLng,
                    17.0f
                )
            )
            // TODO LatLng test
            viewModel.setLocation(currentLocationLatLng)
        }
    }

//    // TODO https://stackoverflow.com/a/45564994
//    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
//        return ContextCompat.getDrawable(context, vectorResId)?.run {
//            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
//            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
//            draw(Canvas(bitmap))
//            BitmapDescriptorFactory.fromBitmap(bitmap)
//        }
//    }

    override fun onResume() {
        super.onResume()
        if (markerCoordinates.isNotEmpty()) {
            markerCoordinates.clear()
        }
    }

    override fun onPause() {
        super.onPause()
        if (markerCoordinates.isNotEmpty()) {
            markerCoordinates.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

