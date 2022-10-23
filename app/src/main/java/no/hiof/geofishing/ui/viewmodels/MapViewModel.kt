package no.hiof.geofishing.ui.viewmodels

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import no.hiof.geofishing.MainActivity
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch

class MapViewModel(catchRepository: Repository<Catch>) : ViewModel() {
    private var catchRepo: Repository<Catch>
    var catchList2: MutableList<Catch> = mutableListOf()
    // Coordinates defaults to HiØ Remmen.
    val defLatitude = 59.12927227233991
    val defLongitude = 11.352814708532474

    val locationPermissionsRequired = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var catchList: LiveData<Response<List<Catch>>>

    init {
        catchRepo = catchRepository
        catchList = catchRepo.read().asLiveData()
    }

    fun setLocation(latLng: LatLng) {
        MainActivity.latitude = latLng.latitude
        MainActivity.longitude = latLng.longitude
    }

}