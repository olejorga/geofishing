package no.hiof.geofishing.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch

class FeedViewModel(catchRepository: Repository<Catch>) : ViewModel() {
    lateinit var catchList : LiveData<Response<List<Catch>>>

    init {
        viewModelScope.launch {
            catchList = catchRepository.read().asLiveData()
        }
    }
}