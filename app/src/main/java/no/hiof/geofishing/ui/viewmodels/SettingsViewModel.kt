package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.data.contracts.AuthService

class SettingsViewModel(authService: AuthService) : ViewModel() {
    private var auth: AuthService
    val authenticated get() = auth.authenticated

    init {
        auth = authService
    }

    suspend fun logout() {
        auth.logout()
    }

}