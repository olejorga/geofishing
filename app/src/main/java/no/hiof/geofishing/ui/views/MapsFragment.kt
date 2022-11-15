package no.hiof.geofishing.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
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
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.R
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.databinding.FragmentMapsBinding
import no.hiof.geofishing.ui.utils.VectorToBitmapDescriptor.bitmapDescriptorFromVector
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.MapViewModel

class MapsFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var fishSpeciesArray: Array<String>
    private lateinit var speciesImgResourceIds: IntArray
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var mGoogleMap: GoogleMap? = null
    private var offsetBy = 0

    // Constant determining offset on duplicate marker LatLngs
    companion object {
        private const val POSITION_OFFSET = 0.00003f
    }

    private val viewModel: MapViewModel by viewModels {
        ViewModelFactory.create {
            MapViewModel(
                (activity?.application as GeofishingApplication).catchRepository
            )
        }
    }

    // Get an array of species from strings.xml and the species marker picture id's from drawable
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fishSpeciesArray = context.resources.getStringArray(R.array.fish_array)
        speciesImgResourceIds = getDrawableIntIds()
    }

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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_menu_maps_fragment_to_menu_catch_fragment)
        }
    }

    // Checks a new marker position against already created markers to prevent overlap.
    // https://stackoverflow.com/a/57282879/20313954
    private fun checkMarkerOverLap(originalMarkerPosition: LatLng): LatLng {
        val updatedMarkerPosition: LatLng

        if (viewModel.markerPositionArray.contains(originalMarkerPosition)) {
            var latitude = 0.0
            var longitude = 0.0
            when (offsetBy) {
                0 -> {
                    latitude = originalMarkerPosition.latitude + POSITION_OFFSET
                    longitude = originalMarkerPosition.longitude
                }
                1 -> {
                    latitude = originalMarkerPosition.latitude + POSITION_OFFSET
                    longitude = originalMarkerPosition.longitude
                }
                2 -> {
                    latitude = originalMarkerPosition.latitude
                    longitude = originalMarkerPosition.longitude + POSITION_OFFSET
                }
                3 -> {
                    latitude = originalMarkerPosition.latitude
                    longitude = originalMarkerPosition.longitude + POSITION_OFFSET
                }
                4 -> {
                    latitude = originalMarkerPosition.latitude + POSITION_OFFSET
                    longitude = originalMarkerPosition.longitude + POSITION_OFFSET
                }
            }
            offsetBy++
            if (offsetBy == 5)
                offsetBy = 0

            updatedMarkerPosition = checkMarkerOverLap(LatLng(latitude, longitude))
        } else {
            viewModel.markerPositionArray.add(originalMarkerPosition)
            updatedMarkerPosition = originalMarkerPosition
        }
        return updatedMarkerPosition
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        if (mGoogleMap == null)
            mGoogleMap = googleMap

        // Starts the process of permission checks
        checkLocationPermissions()

        // Checks view model list against Db then places markers
        viewModel.catchList.observe(viewLifecycleOwner) { response ->
            if (response.error == null && response.data != null) {
                if (viewModel.catchListCache != response.data) {

                    // Changing to MutableList so forEachIndexed can be used
                    viewModel.catchListCache = response.data as MutableList<Catch>
                    var catchLatLng: LatLng

                    viewModel.catchListCache.forEachIndexed { _, catch ->

                        catchLatLng =
                            // Set default coordinates if catch cords are null
                            if (catch.latitude == null || catch.longitude == null)
                                LatLng(viewModel.defLatitude, viewModel.defLongitude)
                            else
                                LatLng(catch.latitude, catch.longitude)

                        // Gets the current species string int value from res/values/strings.xml
                        val currentSpecies = fishSpeciesArray.indexOf(catch.species)
                        // Gets the currentSpecies picture Id from res/values/arrays.xml
                        val markerImgId = speciesImgResourceIds[currentSpecies]
                        // Creates a BitmapDescriptor from a drawable resource defined above
                        val markerIcon =
                            bitmapDescriptorFromVector(requireContext(), markerImgId)
                        // Checks marker coords are unique and creates a new marker.
                        val marker = createMapMarker(catchLatLng, markerIcon)
                        // Set marker tag to id to use in deeplink to view catch details.
                        marker?.tag = catch.id.toString()
                    }
                }
            }
        }
        googleMap.setOnMarkerClickListener(this)
    }

    // parses a uri with an argument from marker to show detail view of clicked catch on map
    override fun onMarkerClick(marker: Marker): Boolean {
        val uri = Uri.parse("myapp://Geofishing.com/${marker.tag}")
        if (findNavController().graph.hasDeepLink(uri)) {
            findNavController().navigate(uri)
        } else {
            // TODO toast?
        }
        return true
    }

    // Gets the int id's of drawable resources
    private fun getDrawableIntIds(): IntArray {
        val toArray = resources.obtainTypedArray(R.array.icons)
        val drawableIntIds = IntArray(toArray.length())
        for (i in drawableIntIds.indices)
            drawableIntIds[i] = toArray.getResourceId(i, 0)

        toArray.recycle()
        return drawableIntIds
    }

    // Creates a marker with a position and a marker icon
    private fun createMapMarker(markerPosition: LatLng, markerIcon: BitmapDescriptor?): Marker? {
        val uniquePosition = checkMarkerOverLap(markerPosition)
        return mGoogleMap?.addMarker(
            MarkerOptions()
                .position(uniquePosition)
                .icon(markerIcon)
        )
    }

    // Sets the current position on map
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
                    20.0f
                )
            )
            viewModel.setLocation(currentLocationLatLng)
        }
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

    /**
     * Requests user to choose between either coarse or fine location, if dismissed shows an
     * AlertDialog of why the app needs these permissions depending on the
     * shouldShowRequestPermissionRationale method.
     */
    private val requestLocationPermissions =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { permissionRequest ->
                val isGranted = permissionRequest.value
                val permission = permissionRequest.key
                when {
                    isGranted -> {
                        setUserLocation()
                        return@registerForActivityResult
                    }
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
                        return@registerForActivityResult
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        mGoogleMap = null
        _binding = null
    }
}

