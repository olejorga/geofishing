package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.flow.toList
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile
import java.util.Dictionary
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import no.hiof.geofishing.data.contracts.Response

class RankViewModel(profileRepository: Repository<Profile>, catchRepository: Repository<Catch>) : ViewModel() {
    lateinit var profileList : LiveData<Response<List<Profile>>>
    lateinit var catchList : LiveData<Response<List<Catch>>>

    fun setPoints(profiles : MutableList<Profile>, catches : List<Catch>) {
        profiles.forEach { p ->
            p.points = catches.count {
                it.profile == p.id
            }
        }

    }

    init {
        viewModelScope.launch {
            profileList = profileRepository.read().asLiveData()
            catchList = catchRepository.read().asLiveData()
        }
    }
}