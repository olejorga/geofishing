package no.hiof.geofishing.data.services

import android.util.Log
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile

/**
 * A firebase implementation of a auth service.
 */
class FirebaseAuthService(
    private val auth: FirebaseAuth,
    private val profileRepository: Repository<Profile>
) : AuthService {
    private val user get() = auth.currentUser
    override val authenticated get() = user != null
    override val email get() = user?.email
    override val id get() = user?.uid

    companion object {
        private val TAG = FirebaseAuthService::class.java.simpleName
    }

    override suspend fun login(email: String, password: String): Response<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.d(TAG, e.toString())
            Response(error = "Wrong username or password.")
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not sign in user.")
        }
    }

    override suspend fun logout(): Response<Unit> {
        return try {
            auth.signOut()
            Response()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not sign out user.")
        }
    }

    override suspend fun signup(email: String, password: String, name: String): Response<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            profileRepository.create(Profile(null, null, name), user!!.uid)
            Response()
        } catch (e: FirebaseAuthWeakPasswordException) {
            Log.d(TAG, e.toString())
            Response(error = "Password is not strong enough.")
        } catch (e: FirebaseAuthUserCollisionException) {
            Log.d(TAG, e.toString())
            Response(error = "Email already in use.")
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not sign up user.")
        }
    }

    override suspend fun changePassword(newPassword: String, oldPassword: String): Response<Unit> {
        return try {
            reAuthenticate(oldPassword)
            user!!.updatePassword(newPassword).await()
            Response()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not change password.")
        }
    }

    override suspend fun changeEmail(newEmail: String, password: String): Response<Unit> {
        return try {
            reAuthenticate(password)
            user!!.updateEmail(newEmail).await()
            Response()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not change email.")
        }
    }

    private suspend fun reAuthenticate(password: String) {
        val credential = EmailAuthProvider.getCredential(user!!.email!!, password)
        user!!.reauthenticate(credential).await()
    }
}