package no.hiof.geofishing.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapViewModel : ViewModel() {
    private lateinit var _callback : OnMapReadyCallback
    init {
        initalMapPosition()
    }

    private fun initalMapPosition() {
        _callback = OnMapReadyCallback { googleMap ->
            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */
            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */

            val hio = LatLng(59.12927227233991, 11.352814708532474)
            googleMap.addMarker(MarkerOptions().position(hio).title("Marker for Høgskolen i Østfold"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hio, 17.0f))
        }
    }


    public val callback get() = _callback
}