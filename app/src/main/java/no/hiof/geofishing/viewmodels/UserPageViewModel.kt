package no.hiof.geofishing.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile

class UserPageViewModel(authService: AuthService, profileRepository: Repository<Profile>) : ViewModel() {
    val profile : LiveData<Response<Profile>>

    init {
        profile = profileRepository.find(authService.id!!).asLiveData()
    }
}