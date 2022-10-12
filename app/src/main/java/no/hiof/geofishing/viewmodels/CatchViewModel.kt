package no.hiof.geofishing.viewmodels

import androidx.lifecycle.ViewModel
import no.hiof.geofishing.data.constants.Specie
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.repositories.CatchRepository
import java.time.Instant
import java.util.*

class CatchViewModel(authService: AuthService, catchRepository : Repository<Catch>) : ViewModel() {
    private var catchRepo : Repository<Catch>
    private var auth : AuthService
    suspend fun createCatch() = catchRepo.create(newCatch(), auth.id)

    init {
        catchRepo = catchRepository
        auth = authService
    }


    private fun newCatch() = Catch(id, date, description, latitude, length, longitude, lure, picture, place, rod, species, title, profile, weight)

        var id: String? = null
        var date: Date? = null
        var description: String? = null
        var latitude: Double? = null
        var length: Int? = null
        var longitude: Double? = null
        var lure: String? = null
        var picture: String? = null
        var place: String? = null
        var rod: String? = null
        var species: Specie? = null
        var title: String? = null
        var profile: String? = null
        var weight: Int? = null
    }
    var catch : Catch? = null
