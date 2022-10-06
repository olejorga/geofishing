package no.hiof.geofishing.services

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.models.Checklist
import no.hiof.geofishing.models.Profile
import no.hiof.geofishing.repositories.ChecklistRepository
import no.hiof.geofishing.repositories.ProfileRepository

object AuthService {
    private val auth get() = Firebase.auth
    val user get() = auth.currentUser

    fun login(email: String, password: String) = flow {
        auth.signInWithEmailAndPassword(email, password).await()
        emit(Unit)
    }

    fun logout() = auth.signOut()

    fun signup(name: String, email: String, password: String) = flow {
        auth.createUserWithEmailAndPassword(email, password).await()

        if (user != null) {
            ProfileRepository.create(Profile(user!!.uid, null, name)).single()
            ChecklistRepository.create(Checklist(user!!.uid)).single()
        }

        emit(Unit)
    }

    private fun reauthenticate(password: String) = flow {
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(user!!.email!!, password)
            user!!.reauthenticate(credential).await()
        }

        emit(Unit)
    }

    fun changePassword(newPassword: String, oldPassword: String) = flow {
        reauthenticate(oldPassword).single()
        user!!.updatePassword(newPassword).await()

        emit(Unit)
    }

    fun changeEmail(newEmail: String, password: String) = flow {
        reauthenticate(password).single()
        user!!.updateEmail(newEmail).await()

        emit(Unit)
    }
}