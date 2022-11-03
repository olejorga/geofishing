package no.hiof.geofishing.data.services

import android.content.Context
import android.util.Log
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.R
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile

/**
 * A firebase implementation of a auth service.
 */
class FirebaseAuthService(
    private val auth: FirebaseAuth,
    private val profileRepository: Repository<Profile>,
    private val context: Context,
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
            Response(error = context.getString(R.string.auth_service_wrong_credentials_error))
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.auth_service_login_error))
        }
    }

    override suspend fun logout(): Response<Unit> {
        return try {
            auth.signOut()
            Response()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.auth_service_logout_error))
        }
    }

    override suspend fun signup(email: String, password: String, name: String): Response<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            profileRepository.create(Profile(null, null, name), user!!.uid)
            Response()
        } catch (e: FirebaseAuthWeakPasswordException) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.auth_service_password_strength_error))
        } catch (e: FirebaseAuthUserCollisionException) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.auth_service_user_exists_error))
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.auth_service_signup_error))
        }
    }

    override suspend fun changePassword(newPassword: String, oldPassword: String): Response<Unit> {
        return try {
            reAuthenticate(oldPassword)
            user!!.updatePassword(newPassword).await()
            Response()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.auth_service_change_password_error))
        }
    }

    override suspend fun changeEmail(newEmail: String, password: String): Response<Unit> {
        return try {
            reAuthenticate(password)
            user!!.updateEmail(newEmail).await()
            Response()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.auth_service_change_email_error))
        }
    }

    private suspend fun reAuthenticate(password: String) {
        val credential = EmailAuthProvider.getCredential(user!!.email!!, password)
        user!!.reauthenticate(credential).await()
    }
}