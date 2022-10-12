package no.hiof.geofishing.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.models.FeedPost

class FeedViewModel : ViewModel() {
    private lateinit var _feedList : MutableList<FeedPost>
    val feedList get() = _feedList

    init {
        retrieveFeed()
    }

    private fun retrieveFeed() {
        _feedList = ArrayList(FeedPost.getFeedPosts())
    }
}