package no.hiof.geofishing.repositories

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.models.Checklist
import no.hiof.geofishing.models.ChecklistItem
import no.hiof.geofishing.models.Profile

object ChecklistRepository {
    private val db get() = Firebase.firestore.collection("checklists")

    fun create(checklist: Checklist) = flow {
        if (checklist.id != null) {
            db.document(checklist.id).set(checklist).await()
            emit(checklist.id)
        } else {
            emit(db.add(checklist).await())
        }
    }

    fun read() = db
        .snapshots()
        .map { it.toObjects<Checklist>() }

    fun update(id: String, checklist: Map<String, Any>) = flow {
        emit(db
            .document(id)
            .update(checklist)
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

    object items {
        fun create(checklistId: String, checklistItem: ChecklistItem) = flow {
            db.document(checklistId)
                .update("items", FieldValue.arrayUnion(checklistItem))
                .await()

            emit(Unit)
        }

        fun update(checklistId: String, checklistItem: ChecklistItem) = flow {
            db.document(checklistId)
                .update("items", FieldValue.arrayRemove(checklistItem))
                .await()

            emit(Unit)
        }

        fun delete(checklistId: String, userId: String) = flow {
            db.document(checklistId)
                .update("items", FieldValue.arrayRemove(userId))
                .await()

            emit(Unit)
        }
    }
}