package no.hiof.geofishing.ui.views

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
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentMapsBinding
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
    private val defLatitude = 59.12927227233991
    private val defLongitude = 11.352814708532474
    private var catchesMarkerList = mutableListOf<LatLng>()

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

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        checkLocationPermissions()

        viewModel.catchList.observe(viewLifecycleOwner) { response ->
            if (response.error == null && response.data != null) {
                val catchList = response.data

                // TODO itte iterer gjennom lista kår gong, og skill på species for forskjellige markers
                catchList.forEach {
                    if (it.latitude != null && it.longitude != null) {
                        var markerColor = 0.0F
                        when (it.species) {
                            resources.getStringArray(R.array.fish_array)[0] -> markerColor =
                                BitmapDescriptorFactory.HUE_RED
                            resources.getStringArray(R.array.fish_array)[1] -> markerColor =
                                BitmapDescriptorFactory.HUE_ORANGE
                            resources.getStringArray(R.array.fish_array)[2] -> markerColor =
                                BitmapDescriptorFactory.HUE_YELLOW
                            resources.getStringArray(R.array.fish_array)[3] -> markerColor =
                                BitmapDescriptorFactory.HUE_GREEN
                            resources.getStringArray(R.array.fish_array)[4] -> markerColor =
                                BitmapDescriptorFactory.HUE_CYAN
                            resources.getStringArray(R.array.fish_array)[5] -> markerColor =
                                BitmapDescriptorFactory.HUE_AZURE
                            resources.getStringArray(R.array.fish_array)[6] -> markerColor =
                                BitmapDescriptorFactory.HUE_BLUE
                            resources.getStringArray(R.array.fish_array)[7] -> markerColor =
                                BitmapDescriptorFactory.HUE_VIOLET
                        }

                        // TODO legg te navigering ved trykk via marker.tag te catchID?
//                        var marker: Marker? =
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(it.latitude, it.longitude))
                                .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                        )
//                        marker?.tag =
                        // catchesMarkerList.add(LatLng(it.latitude, it.longitude))
                    } else
                        return@forEach
                }
            }
//            catchesMarkerList.forEach{
//                mGoogleMap!!.addMarker(
//                    MarkerOptions()
//                        .position(it)
//                )
//            }
        }

//        googleMap.setOnMarkerClickListener(this)
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
                            }.setNegativeButton(R.string.deny_permission_dialog, null).create()
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
                    location?.latitude ?: defLatitude,
                    location?.longitude ?: defLongitude
                )
            mGoogleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentLocationLatLng,
                    17.0f
                )
            )
            // TODO LatLng test
            viewModel.setLocation(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
    }

}
