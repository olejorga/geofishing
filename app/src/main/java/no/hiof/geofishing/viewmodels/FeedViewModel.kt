package no.hiof.geofishing.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.models.FeedPost

class FeedViewModel : ViewModel() {
    private lateinit var feedList : MutableList<FeedPost>

    init {
        retrieveFeed()
    }

    private fun retrieveFeed() {
        feedList = ArrayList(FeedPost.getFeedPosts())
    }

    fun getFeed() : MutableList<FeedPost> {
        return feedList
    }
}