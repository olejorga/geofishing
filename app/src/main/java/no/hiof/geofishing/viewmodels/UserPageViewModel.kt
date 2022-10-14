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
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.data.services.FirebaseAuthService

class UserPageViewModel(authService: AuthService, profileRepository: Repository<Profile>, catchRepository: Repository<Catch>) : ViewModel() {
    lateinit var profile : LiveData<Response<Profile>>
    lateinit var catches : LiveData<Response<List<Catch>>>

    init {
        viewModelScope.launch {
            if(authService.authenticated == true){
                profile = profileRepository.find(authService.id.toString()).asLiveData()
                catches = catchRepository.search("profile", authService.id.toString()).asLiveData()
            }
        }
    }
}