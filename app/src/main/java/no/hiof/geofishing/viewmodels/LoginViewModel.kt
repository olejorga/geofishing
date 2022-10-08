package no.hiof.geofishing.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.services.FirebaseAuthService

class LoginViewModel(authService: AuthService) : ViewModel() {
    private var auth: AuthService
    val authenticated get() = auth.authenticated

    init {
        auth = authService
    }

    suspend fun login(email: String, password: String) =
        auth.login(email, password)
}
