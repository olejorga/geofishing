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

class ChangePasswordViewModel(
    authService: AuthService,
) : ViewModel() {
    val auth = authService

    init {
    }

    suspend fun changePassword() {

        if (password() && oldPassword.isNotEmpty()) {
            auth.changePassword(password, oldPassword)
        }

    }

    private fun password(): Boolean {
        return (
                password.isNotEmpty()
                        &&
                        passwordConfirm.isNotEmpty()
                        &&
                        password.equals(passwordConfirm))
    }


    var password: String = ""
    var passwordConfirm: String = ""
    var oldPassword: String = ""
}