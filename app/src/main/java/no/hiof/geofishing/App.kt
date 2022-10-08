package no.hiof.geofishing

import android.app.Application
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.data.entities.Subscription
import no.hiof.geofishing.data.entities.Todo
import no.hiof.geofishing.data.repositories.CatchRepository
import no.hiof.geofishing.data.repositories.ProfileRepository
import no.hiof.geofishing.data.repositories.SubscriptionRepository
import no.hiof.geofishing.data.repositories.TodoRepository
import no.hiof.geofishing.data.services.FirebaseAuthService

class App: Application() {
    // Dependencies
    private var _authService = FirebaseAuthService
    val authService: AuthService get() = _authService
    val catchRepository: Repository<Catch> get() = CatchRepository
    val profileRepository: Repository<Profile> get() = ProfileRepository
    val subscriptionRepository: Repository<Subscription> get() = SubscriptionRepository
    val todoRepository: Repository<Todo> get() = TodoRepository

    init {
        // Injecting implementation of a profile repository into auth service.
        _authService.profileRepository = profileRepository
    }
}