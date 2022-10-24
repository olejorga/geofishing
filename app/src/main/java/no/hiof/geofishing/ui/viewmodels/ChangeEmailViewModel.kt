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

class ChangeEmailViewModel(
    authService: AuthService,
) : ViewModel() {
    val auth = authService

    init {
    }

    suspend fun changeEmail() {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.changeEmail(email, password)
        }
    }


    var email: String = ""
    var password: String = ""
}