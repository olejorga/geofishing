package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile

class FeedViewModel(
    catchRepository: Repository<Catch>,
    private val profileRepository: Repository<Profile>
    ) : ViewModel() {
    lateinit var catchList: LiveData<Response<List<Catch>>>
    lateinit var profileList: LiveData<Response<List<Profile>>>

    fun setAllProfiles(catches: List<Catch>, profiles: List<Profile>) {
        catches.forEach { catch ->
            catch.profileName = profiles.find { profile ->
                profile.id == catch.profile
            }?.name.toString()
        }
    }

    fun setSingleProfile(catch: Catch, profiles: List<Profile>){
        catch.profileName = profiles.find { profile ->
            profile.id == catch.profile
        }?.name.toString()
    }

    init {
        viewModelScope.launch {
            catchList = catchRepository.read().asLiveData()
            profileList = profileRepository.read().asLiveData()
        }
    }
}