package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.data.contracts.AuthService

class LoginViewModel(authService: AuthService) : ViewModel() {
    private var auth: AuthService
    val authenticated get() = auth.authenticated

    init {
        auth = authService
    }

    suspend fun login(email: String, password: String) =
        auth.login(email, password)
}
