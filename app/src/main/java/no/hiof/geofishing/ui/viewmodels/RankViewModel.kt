package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.flow.toList
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile
import java.util.Dictionary
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import no.hiof.geofishing.data.contracts.Response

class RankViewModel(
    catchRepository: Repository<Catch>,
    profileRepository: Repository<Profile>,
) : ViewModel() {
    val ranks = catchRepository.read()
        .combine(profileRepository.read()) { resCatches, resProfiles ->
            val ranks = ArrayList<Rank>()

            if (
                resCatches.error == null && resCatches.data != null &&
                resProfiles.error == null && resProfiles.data != null
            ) {
                for (profile in resProfiles.data) {
                    var count = 0

                    for (catch in resCatches.data) {
                        if (profile.id == catch.profile)
                            count++
                    }

                    ranks.add(Rank(profile, count))
                }
            }

            return@combine ranks
        }.asLiveData()

    data class Rank(
        val profile: Profile,
        val points: Int,
    )
}