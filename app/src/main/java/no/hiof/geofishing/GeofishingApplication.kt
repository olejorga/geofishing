package no.hiof.geofishing

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.FileService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.data.entities.Todo
import no.hiof.geofishing.data.repositories.CatchRepository
import no.hiof.geofishing.data.repositories.ProfileRepository
import no.hiof.geofishing.data.repositories.TodoRepository
import no.hiof.geofishing.data.services.FirebaseAuthService
import no.hiof.geofishing.data.services.FirebaseFileService

/**
 * The main entry point of the app.
 * - This is where all dependencies are injected.
 * - This way no duplicates are instantiated later in other places.
 * - All class dependencies are instantiated lazily.
 */
class GeofishingApplication : Application() {
    val catchRepository by lazy<Repository<Catch>> {
        CatchRepository(Firebase.firestore.collection("catches"), this)
    }

    val profileRepository by lazy<Repository<Profile>> {
        ProfileRepository(Firebase.firestore.collection("profiles"), this)
    }

    val todoRepository by lazy<Repository<Todo>> {
        TodoRepository(Firebase.firestore.collection("todos"), this)
    }

    val authService by lazy<AuthService> {
        FirebaseAuthService(Firebase.auth, profileRepository, this)
    }

    val fileService by lazy<FileService> {
        FirebaseFileService(Firebase.storage, this)
    }
}