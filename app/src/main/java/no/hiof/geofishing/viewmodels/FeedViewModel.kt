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
//    private lateinit var _feedList : MutableList<FeedPost>
//    val feedList get() = _feedList
    lateinit var catchList : LiveData<Response<List<Catch>>>

    init {
//        retrieveFeed()
        viewModelScope.launch {
            catchList = catchRepository.read().asLiveData()
            Log.d("FEED",catchRepository.read().collect().toString())
        }

    }

//    private fun retrieveFeed() {
//        _feedList = ArrayList(FeedPost.getFeedPosts())
//    }
}