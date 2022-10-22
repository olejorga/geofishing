package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Profile

class RankViewModel(profileRepository: Repository<Profile>) : ViewModel() {
    val profileList = profileRepository.read().asLiveData()
}