package no.hiof.geofishing.data.contracts

import android.net.Uri
import java.io.File

interface FileService {
    /**
     * Asynchronously upload a file.
     *
     * @param filename The name of the file once stored (excl. extension).
     * @return A standardized response (Data is the url of the file).
     */
    suspend fun upload(filename: String, uri: Uri): Response<String>

    /**
     * Asynchronously delete file.
     *
     * @param filename The name of the file to delete (incl. extension).
     * @return A standardized response.
     */
    suspend fun delete(filename: String): Response<Unit>
}