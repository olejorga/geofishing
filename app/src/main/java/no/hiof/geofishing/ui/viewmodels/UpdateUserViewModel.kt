package no.hiof.geofishing.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.FileService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile
import java.util.*

class UpdateUserViewModel(
    authService: AuthService,
    fileService: FileService,
    profileRepository: Repository<Profile>
    ) : ViewModel() {
    lateinit var profile: LiveData<Response<Profile>>
    val auth = authService
    val fileService = fileService
    val profileRepo = profileRepository

    init {
        viewModelScope.launch {
            profile = profileRepository.find(authService.id.toString()).asLiveData()
        }
    }

    suspend fun updateUser() {
        if (name.isNotEmpty()){
            mapOf("name" to name).let { profileRepo.update(auth.id.toString(), it) }
        }
        if (bio.isNotEmpty()){
            mapOf("bio" to bio).let { profileRepo.update(auth.id.toString(), it) }
        }
        if (picture.value != null){
            val filename = UUID.randomUUID().toString()
            val (data, error) = fileService.upload(filename, _picture.value!!)

            if (error != null) {
                Log.d("PICTURE", "Error saving picture to fileSystem")
                return
            }

            mapOf("portrait" to data.toString()).let { profileRepo.update(auth.id.toString(), it) }
        }
//        if (password()) {
//            auth.changePassword(password, "old")
//        }
//        if (email.isNotEmpty()){
//        }
    }

    private fun password(): Boolean {
        return (
                password.isNullOrEmpty() && passwordConfirm.isNullOrEmpty()
                        ||
                password.equals(passwordConfirm))
    }

    private val _picture = MutableLiveData<Uri>()
    val picture: LiveData<Uri> get() = _picture
    fun setPicture(uri: Uri) {
        _picture.value = uri
    }

    var name: String = ""
    var bio: String = ""
    var email: String = ""
    var pictureUrl: String = ""
    var password: String = ""
    var passwordConfirm: String = ""
}