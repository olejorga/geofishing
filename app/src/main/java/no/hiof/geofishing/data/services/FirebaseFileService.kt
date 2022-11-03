package no.hiof.geofishing.data.services

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.data.contracts.FileService
import no.hiof.geofishing.data.contracts.Response

/**
 * A firebase implementation of a file service.
 */
class FirebaseFileService(
    private val storage: FirebaseStorage
) : FileService {

    companion object {
        private val TAG = FirebaseFileService::class.java.simpleName
    }

    override suspend fun upload(filename: String, uri: Uri): Response<String> {
        return try {
            val ref = storage.reference.child(filename)
            ref.putFile(uri).await()

            Response(ref.downloadUrl.await().toString())
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not upload file.")
        }
    }

    override suspend fun delete(filename: String): Response<Unit> {
        return try {
            val ref = storage.reference.child(filename)
            ref.delete().await()

            Response()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Response(error = "Could not delete file.")
        }
    }
}
