package no.hiof.geofishing.data.services

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.data.repositories.ProfileRepository

object FirebaseAuthService: AuthService {
    private val auth get() = Firebase.auth
    private val user get() = auth.currentUser
    private val profiles get() = ProfileRepository
    override val authenticated get() = user != null
    override val email get() = user?.email

    override suspend fun login(email: String, password: String): Response<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response()
        } catch (e: Exception) {
            Log.d(Tags.AUTH_SERVICE.toString(), e.toString())
            Response(error = "Could not sign in user.")
        }
    }

    override suspend fun logout(): Response<Unit> {
        return try {
            auth.signOut()
            Response()
        } catch (e: Exception) {
            Log.d(Tags.AUTH_SERVICE.toString(), e.toString())
            Response(error = "Could not sign out user.")
        }
    }

    override suspend fun signup(email: String, password: String, name: String): Response<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            profiles.create(Profile(user!!.uid, null, name))
            Response()
        } catch (e: Exception) {
            Log.d(Tags.AUTH_SERVICE.toString(), e.toString())
            Response(error = "Could not sign up user.")
        }
    }

    override suspend fun changePassword(newPassword: String, oldPassword: String): Response<Unit> {
        return try {
            reauthenticate(oldPassword)
            user!!.updatePassword(newPassword).await()
            Response()
        } catch (e: Exception) {
            Log.d(Tags.AUTH_SERVICE.toString(), e.toString())
            Response(error = "Could not change password.")
        }
    }

    override suspend fun changeEmail(newEmail: String, password: String): Response<Unit> {
        return try {
            reauthenticate(password)
            user!!.updateEmail(newEmail).await()
            Response()
        } catch (e: Exception) {
            Log.d(Tags.AUTH_SERVICE.toString(), e.toString())
            Response(error = "Could not change email.")
        }
    }

    private suspend fun reauthenticate(password: String) {
        val credential = EmailAuthProvider.getCredential(user!!.email!!, password)
        user!!.reauthenticate(credential).await()
    }
}