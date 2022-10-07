package no.hiof.geofishing.data.repositories

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile

object ProfileRepository: Repository<Profile> {
    private val database get() = Firebase.firestore.collection("profiles")

    override suspend fun create(entity: Profile, id: String?): Response<String> {
        return try {
            if (id != null) {
                database.document(id).set(entity).await()
                Response(id)
            } else
                Response(database.add(entity).await().id)
        } catch (e: Exception) {
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            Response(error = "Could not create profile.")
        }
    }

    override fun read() = database
        .snapshots()
        .mapNotNull { Response(it.toObjects<Profile>()) }
        .catch { e ->
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            emit(Response(error = "Could not get profiles."))
        }

    override suspend fun update(id: String, data: Map<String, Any>): Response<Unit> {
        return try {
            database.document(id).update(data).await()
            Response(Unit)
        } catch (e: Exception) {
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            Response(error = "Could not update profile.")
        }
    }

    override suspend fun delete(id: String): Response<Unit> {
        return try {
            database.document(id).delete().await()
            Response(Unit)
        } catch (e: Exception) {
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            Response(error = "Could not delete profile.")
        }
    }

    override fun find(key: String, value: Any) = database
        .whereEqualTo(key, value)
        .snapshots()
        .mapNotNull { Response(it.toObjects<Profile>()) }
        .catch { e ->
            Log.d(Tags.REPOSITORY.toString(), e.toString())
            emit(Response(error = "Could not find any matching profile."))
        }
}