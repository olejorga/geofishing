package no.hiof.geofishing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.data.services.FirebaseAuthService
import no.hiof.geofishing.models.User

class RankViewModel(profileRepository : Repository<Profile>) : ViewModel() {
    private lateinit var _userList : MutableList<User>
    val userList get() = _userList
    val profileList = profileRepository.read().asLiveData()

    init {
        retrieveRankedList()
    }

    private fun retrieveRankedList() {
        _userList = ArrayList(User.getUsers().sortedBy { user -> user.points}.reversed())
    }
}