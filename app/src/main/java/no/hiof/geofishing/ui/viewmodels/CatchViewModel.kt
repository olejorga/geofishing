package no.hiof.geofishing.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.FileService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch
import java.util.*

class CatchViewModel(
    authService: AuthService,
    private val catchRepository: Repository<Catch>,
    private val fileService: FileService
) : ViewModel() {

    var date: Date? = null
    var description: String? = null
    var latitude: Double? = null
    var length: Int? = null
    var longitude: Double? = null
    var lure: String? = null
    var place: String? = null
    var rod: String? = null
    var species: String? = null
    var title: String? = null
    val profile: String? = authService.id
    var weight: Int? = null

    private val _picture = MutableLiveData<Uri>()
    val picture: LiveData<Uri> get() = _picture

    fun setPicture(uri: Uri) {
        _picture.value = uri
    }

    suspend fun createCatch(): Response<String> {
        var url: String? = null

        if (_picture.value != null) {
            val filename = UUID.randomUUID().toString()
            val (data, error) = fileService.upload(filename, _picture.value!!)

            if (error != null) {
                return Response(null, error)
            }

            url = data
        }

        val catch = Catch(
            id = null,
            date,
            description,
            latitude,
            length,
            longitude,
            lure,
            picture = url,
            place,
            rod,
            species,
            title,
            profile,
            weight
        )

        return catchRepository.create(catch)
    }

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
}
