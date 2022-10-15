package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.ui.models.User

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