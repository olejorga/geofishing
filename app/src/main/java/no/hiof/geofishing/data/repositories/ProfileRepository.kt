package no.hiof.geofishing.data.repositories

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.R
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile

/**
 * A firebase implementation of a profile repository.
 */
class ProfileRepository(
    private val collection: CollectionReference,
    private val context: Context,
) : Repository<Profile> {

    companion object {
        private val TAG = ProfileRepository::class.java.simpleName
    }

    override suspend fun create(entity: Profile, id: String?): Response<String> {
        return try {
            if (id != null) {
                this.collection.document(id).set(entity).await()
                Response(id)
            } else
                Response(this.collection.add(entity).await().id)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.profile_repo_create_error))
        }
    }

    override fun read() = this.collection
        .snapshots()
        .mapNotNull { Response(it.toObjects<Profile>()) }
        .catch { e ->
            Log.d(TAG, e.toString())
            emit(Response(error = context.getString(R.string.profile_repo_read_error)))
        }

    override suspend fun update(id: String, data: Map<String, Any>): Response<Unit> {
        return try {
            this.collection.document(id).update(data).await()
            Response(Unit)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.profile_repo_update_error))
        }
    }

    override suspend fun delete(id: String): Response<Unit> {
        return try {
            this.collection.document(id).delete().await()
            Response(Unit)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = context.getString(R.string.profile_repo_delete_error))
        }
    }

    override fun find(id: String) = this.collection
        .document(id)
        .snapshots()
        .mapNotNull { Response(it.toObject<Profile>()) }
        .catch { e ->
            Log.d(TAG, e.toString())
            emit(Response(error = context.getString(R.string.profile_repo_find_error)))
        }

    override fun search(property: String, value: Any) = this.collection
        .whereEqualTo(property, value)
        .snapshots()
        .mapNotNull { Response(it.toObjects<Profile>()) }
        .catch { e ->
            Log.d(TAG, e.toString())
            emit(Response(error = context.getString(R.string.profile_repo_search_error)))
        }
}