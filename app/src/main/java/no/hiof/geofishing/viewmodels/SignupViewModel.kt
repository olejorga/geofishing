package no.hiof.geofishing.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.data.contracts.AuthService

class SignupViewModel(authService: AuthService) : ViewModel() {
    private var auth: AuthService
    val authenticated get() = auth.authenticated

    init {
        auth = authService
    }

    suspend fun signup(email: String, password: String, name: String) =
        auth.signup(email, password, name)
}