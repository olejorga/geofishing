package no.hiof.geofishing.data.services

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.contracts.FileService
import no.hiof.geofishing.data.contracts.Response
import java.io.File

object FirebaseFileService: FileService {
    // Creating a getter for retrieving the firebase instance (singleton).
    private val storage get() = Firebase.storage

    override suspend fun upload(file: File): Response<String> {
        return try {
            val uri = Uri.fromFile(file)
            val ref = storage.reference.child(uri.lastPathSegment!!)

            ref.putFile(uri).await()

            Response(ref.downloadUrl.await().toString())
        } catch (e: Exception) {
            Log.d(Tags.FILE_SERVICE.toString(), e.toString())
            Response(error = "Could not upload file.")
        }
    }

    override suspend fun delete(filename: String): Response<Unit> {
        return try {
            val ref = storage.reference.child(filename)

            ref.delete().await()

            Response()
        } catch (e: Exception) {
            Log.d(Tags.FILE_SERVICE.toString(), e.toString())
            Response(error = "Could not delete file.")
        }
    }
}
