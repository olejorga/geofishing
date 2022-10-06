package no.hiof.geofishing.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.models.Catch

object CatchRepository {
    private val db get() = Firebase.firestore.collection("catches")

    fun create(catch: Catch) = flow {
        if (catch.id != null) {
            db.document(catch.id).set(catch).await()
            emit(catch.id)
        } else {
            emit(db.add(catch).await())
        }
    }

    fun read() = db
        .snapshots()
        .map { it.toObjects<Catch>() }

    fun update(id: String, catch: Map<String, Any>) = flow {
        emit(db
            .document(id)
            .update(catch)
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
        .map { it.toObject<Catch>() }
}