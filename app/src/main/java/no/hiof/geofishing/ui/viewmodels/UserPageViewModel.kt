package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile

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