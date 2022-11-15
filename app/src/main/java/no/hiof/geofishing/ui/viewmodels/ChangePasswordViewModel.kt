package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Response

class ChangePasswordViewModel(
    authService: AuthService,
) : ViewModel() {
    val auth = authService

    init {
    }

    suspend fun changePassword(password: String, confirmPassword: String, oldPassword: String) : Response<Unit> {
        if (password == confirmPassword){
            return auth.changePassword(password, oldPassword)
        }
        return Response(null, error = "Passwords do not match.")
    }
}