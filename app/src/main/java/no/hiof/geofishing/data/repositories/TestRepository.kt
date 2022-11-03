package no.hiof.geofishing.data.repositories

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch

/**
 * A firebase implementation of a catch repository.
 */
class TestRepository(
    private val collection: CollectionReference
) : Repository<Catch> {

    companion object {
        private val TAG = TestRepository::class.java.simpleName
    }

    init {
        Log.d("HERE", "INIT!")
    }

    override suspend fun create(entity: Catch, id: String?): Response<String> {
        return try {
            if (id != null) {
                this.collection.document(id).set(entity).await()
                Response(id)
            } else
                Response(this.collection.add(entity).await().id)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not create catch.")
        }
    }

    override fun read() = this.collection
        .snapshots()
        .mapNotNull { Response(it.toObjects<Catch>()) }
        .catch { e ->
            Log.d(TAG, e.toString())
            emit(Response(error = "Could not get catches."))
        }

    override suspend fun update(id: String, data: Map<String, Any>): Response<Unit> {
        return try {
            this.collection.document(id).update(data).await()
            Response(Unit)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not update catch.")
        }
    }

    override suspend fun delete(id: String): Response<Unit> {
        return try {
            this.collection.document(id).delete().await()
            Response(Unit)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not delete catch.")
        }
    }

    override fun find(id: String) = this.collection
        .document(id)
        .snapshots()
        .mapNotNull { Response(it.toObject<Catch>()) }
        .catch { e ->
            Log.d(TAG, e.toString())
            emit(Response(error = "Could not find catch with id=$id."))
        }

    override fun search(property: String, value: Any) = this.collection
        .whereEqualTo(property, value)
        .snapshots()
        .mapNotNull { Response(it.toObjects<Catch>()) }
        .catch { e ->
            Log.d(TAG, e.toString())
            emit(Response(error = "Could not find any matching catch."))
        }
}