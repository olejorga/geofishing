package no.hiof.geofishing.ui.viewmodels

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.MainActivity
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch

class MapViewModel(catchRepository: Repository<Catch>) : ViewModel() {
    val locationPermissionsRequired = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    lateinit var catchList : LiveData<Response<List<Catch>>>

    init {
        viewModelScope.launch {
            catchList = catchRepository.read().asLiveData()
        }


    }

    fun setLocation(lat: Double, long: Double) {
        MainActivity.latitude = lat
        MainActivity.longitude = long
    }


}