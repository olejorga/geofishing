package no.hiof.geofishing

import android.app.Application
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.FileService
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
import no.hiof.geofishing.data.services.FirebaseFileService

/**
 * The main entry point of the app, this is where all dependencies are injected.
 */
class App: Application() {
    // The auth service will be initialized at app boot,
    // which is OK since the service is required right after boot to check auth status.
    private val _authService = FirebaseAuthService
    val authService: AuthService get() = _authService

    // Using getters to delay lazy loading.
    val catchRepository: Repository<Catch> get() = CatchRepository
    val profileRepository: Repository<Profile> get() = ProfileRepository
    val subscriptionRepository: Repository<Subscription> get() = SubscriptionRepository
    val todoRepository: Repository<Todo> get() = TodoRepository

    val fileService: FileService get() = FirebaseFileService

    init {
        // Injecting implementation of a profile repository into auth service.
        _authService.profileRepository = profileRepository
    }

    // TODO: Make all error messages strings!
}