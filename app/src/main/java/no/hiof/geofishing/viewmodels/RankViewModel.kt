package no.hiof.geofishing.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.models.User

class RankViewModel : ViewModel() {
    private lateinit var _userList : MutableList<User>
    public val userList get() = _userList

    init {
        retrieveRankedList()
    }

    private fun retrieveRankedList() {
        _userList = ArrayList(User.getUsers().sortedBy { user -> user.points}.reversed())
    }
}