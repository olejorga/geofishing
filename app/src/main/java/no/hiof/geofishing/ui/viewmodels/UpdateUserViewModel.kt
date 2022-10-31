package no.hiof.geofishing.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.FileService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile
import java.util.*

class UpdateUserViewModel(
    private val authService: AuthService,
    private val fileService: FileService,
    private val profileRepository: Repository<Profile>
) : ViewModel() {
    lateinit var profile: LiveData<Response<Profile>>

    init {
        viewModelScope.launch {
            profile = profileRepository.find(authService.id.toString()).asLiveData()
        }
    }

    suspend fun updateUser() {
        if (name.isNotEmpty()) {
            mapOf("name" to name).let { profileRepository.update(authService.id.toString(), it) }
        }
        if (bio.isNotEmpty()) {
            mapOf("bio" to bio).let { profileRepository.update(authService.id.toString(), it) }
        }
        if (_picture.value != null) {
            val filename = UUID.randomUUID().toString()
            Log.d("HERE", filename + " " + _picture.value.toString())
            val (data, error) = fileService.upload(filename, _picture.value!!)

            if (error != null) {
                Log.d("PICTURE", "Error saving picture to fileSystem")
                return
            }

            mapOf("portrait" to data.toString()).let { profileRepository.update(authService.id.toString(), it) }
        }
    }


    private val _picture = MutableLiveData<Uri>()
    val picture: LiveData<Uri> get() = _picture
    fun setPicture(uri: Uri) {
        _picture.value = uri
    }

    var name: String = ""
    var bio: String = ""
}