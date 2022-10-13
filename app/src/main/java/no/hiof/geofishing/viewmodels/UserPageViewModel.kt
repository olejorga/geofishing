package no.hiof.geofishing.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.data.services.FirebaseAuthService

class UserPageViewModel(authService: AuthService, profileRepository: Repository<Profile>) : ViewModel() {
    val profile : LiveData<Response<Profile>>

    init {
        // TODO: Rydd opp her
        Log.d("Before scope", authService.id.toString())
        viewModelScope.launch {
            Log.d("Før", authService.id.toString())
            if(authService.authenticated == true){
                profileRepository.find(authService.id.toString()).collect {
                    Log.d("UserpageViewModel",it.toString())
                }
            }
        }
        profile = profileRepository.find(authService.id.toString()).asLiveData()

    }
}