package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile

class FeedViewModel(catchRepository: Repository<Catch>, profileRepository: Repository<Profile>, authService: AuthService) : ViewModel() {
    private var auth: AuthService
    val currentProfileId get() = auth.id

    lateinit var catchList: LiveData<Response<List<Catch>>>
    lateinit var profileList: LiveData<Response<List<Profile>>>

    fun setAllProfileNames(catches: List<Catch>, profiles: List<Profile>) {
        catches.forEach { catch ->
            catch.profileName = profiles.find { profile ->
                profile.id == catch.profile
            }?.name.toString()
        }
    }

    fun setSingleProfileName(catch: Catch, profiles: List<Profile>){
        catch.profileName = profiles.find { profile ->
            profile.id == catch.profile
        }?.name.toString()
    }

    init {
        auth = authService
        viewModelScope.launch {
            catchList = catchRepository.read().asLiveData()
            profileList = profileRepository.read().asLiveData()
        }
    }
}