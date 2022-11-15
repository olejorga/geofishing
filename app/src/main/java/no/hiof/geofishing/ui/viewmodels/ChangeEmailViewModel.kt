package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Response

class ChangeEmailViewModel(
    authService: AuthService,
) : ViewModel() {
    val auth = authService

    init {
    }

    suspend fun changeEmail(email: String, password: String): Response<Unit> {
        return auth.changeEmail(email, password)
    }
}