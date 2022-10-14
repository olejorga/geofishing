package no.hiof.geofishing.data.repositories

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
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
object CatchRepository: Repository<Catch> {
    // Creating a getter for retrieving the firebase instance (singleton).
    private val database get() = Firebase.firestore.collection("catches")

    override suspend fun create(entity: Catch, id: String?): Response<String> {
        return try {
            if (id != null) {
                database.document(id).set(entity).await()
                Response(id)
            } else
                Response(database.add(entity).await().id)
        } catch (e: Exception) {
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            Response(error = "Could not create catch.")
        }
    }

    override fun read() = database
        .snapshots()
        .mapNotNull { Response(it.toObjects<Catch>()) }
        .catch { e ->
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            emit(Response(error = "Could not get catches."))
        }

    override suspend fun update(id: String, data: Map<String, Any>): Response<Unit> {
        return try {
            database.document(id).update(data).await()
            Response(Unit)
        } catch (e: Exception) {
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            Response(error = "Could not update catch.")
        }
    }

    override suspend fun delete(id: String): Response<Unit> {
        return try {
            database.document(id).delete().await()
            Response(Unit)
        } catch (e: Exception) {
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            Response(error = "Could not delete catch.")
        }
    }

    override fun find(id: String) = database
        .document(id)
        .snapshots()
        .mapNotNull { Response(it.toObject<Catch>()) }
        .catch { e ->
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            emit(Response(error = "Could not find catch with id=$id."))
        }

    override fun search(property: String, value: Any) = database
        .whereEqualTo(property, value)
        .snapshots()
        .mapNotNull { Response(it.toObjects<Catch>()) }
        .catch { e ->
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            emit(Response(error = "Could not find any matching catch."))
        }
}