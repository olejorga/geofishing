package no.hiof.geofishing.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.FileService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Catch
import java.io.File
import java.util.*

class CatchViewModel(authService: AuthService, catchRepository : Repository<Catch>, fileService: FileService) : ViewModel() {
    private var catchRepo : Repository<Catch>
    private var fileService: FileService

    private val _image = MutableLiveData<Uri>()
    val image: LiveData<Uri> get() = _image

    fun setImage(uri: Uri) {
        _image.value = uri
    }

    init {
        catchRepo = catchRepository
        this.fileService = fileService
    }

    suspend fun createCatch() = catchRepo.create(newCatch())

    private fun newCatch() = Catch(null, date, description, latitude, length, longitude, lure, picture, place, rod, species, title, profile, weight)

//    private fun clear() {
//        id = null
//        date = null
//        description = null
//        latitude = null
//        length = null
//        longitude = null
//        lure = null
//        picture = null
//        place = null
//        rod = null
//        species = null
//        title = null
//        profile = null
//        weight = null
//
//    }

    var id: String? = null
    var date: Date? = null
    var description: String? = null
    var latitude: Double? = null
    var length: Int? = null
    var longitude: Double? = null
    var lure: String? = null
    var picture: String? = null
    var place: String? = null
    var rod: String? = null
    var species: String? = null
    var title: String? = null
    var profile: String? = authService.id
    var weight: Int? = null
}
