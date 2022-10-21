package no.hiof.geofishing.ui.viewmodels

import android.provider.ContactsContract.Profile
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response

class UpdateUserViewModel(profileRepository: Repository<Profile>) : ViewModel() {
    lateinit var profile: LiveData<Response<Profile>>

    init {
        viewModelScope.launch {

        }
    }
}