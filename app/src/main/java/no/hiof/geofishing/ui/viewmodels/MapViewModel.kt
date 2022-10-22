package no.hiof.geofishing.ui.viewmodels

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import no.hiof.geofishing.MainActivity
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.repositories.CatchRepository

class MapViewModel(catchRepository: Repository<Catch>) : ViewModel() {
    private var catchRepo: Repository<Catch>

    // default coordinates HIØ
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