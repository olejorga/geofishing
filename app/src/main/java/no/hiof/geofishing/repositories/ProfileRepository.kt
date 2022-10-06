package no.hiof.geofishing.repositories

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.models.Profile

object ProfileRepository {
    private val db get() = Firebase.firestore.collection("profiles")

    fun create(profile: Profile) = flow {
        if (profile.id != null) {
            db.document(profile.id).set(profile).await()
            emit(profile.id)
        } else {
            emit(db.add(profile).await())
        }
    }

    fun read() = db
        .snapshots()
        .map { it.toObjects<Profile>() }

    fun update(id: String, user: Map<String, Any>) = flow {
        emit(db
            .document(id)
            .update(user)
            .await())
    }

    fun delete(id: String) = flow {
        emit(db
            .document(id)
            .delete()
            .await())
    }

    fun find(id: String) = db
        .document(id)
        .snapshots()
        .map { it.toObject<Profile>() }

    object subscriptions {
        fun create(profileId: String, userId: String) = flow {
            db.document(profileId)
                .update("subscriptions", FieldValue.arrayUnion(profileId))
                .await()

            emit(Unit)
        }

        fun delete(profileId: String, userId: String) = flow {
            db.document(profileId)
                .update("subscriptions", FieldValue.arrayRemove(profileId))
                .await()

            emit(Unit)
        }
    }
}