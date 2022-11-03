package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.combine
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile

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