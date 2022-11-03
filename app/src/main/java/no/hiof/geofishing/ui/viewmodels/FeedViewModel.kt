package no.hiof.geofishing.ui.viewmodels

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile

class FeedViewModel(
    catchRepository: Repository<Catch>,
    profileRepository: Repository<Profile>,
) : ViewModel() {
    val posts = catchRepository.read()
        .combine(profileRepository.read()) { resCatches, resProfiles ->
            val posts = ArrayList<Post>()

            if (
                resCatches.error == null && resCatches.data != null &&
                resProfiles.error == null && resProfiles.data != null
            ) {
                for (catch in resCatches.data) {
                    for (profile in resProfiles.data) {
                        if (profile.id == catch.profile)
                            posts.add(Post(catch, profile))
                    }
                }
            }

            return@combine posts
        }.asLiveData()

    data class Post(
        val catch: Catch,
        val profile: Profile,
    )
}