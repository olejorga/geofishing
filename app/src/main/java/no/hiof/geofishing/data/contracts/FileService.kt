package no.hiof.geofishing.data.contracts

import java.io.File

interface FileService {
    /**
     * Asynchronously upload a file.
     *
     * @param file The file to upload.
     * @return A standardized response (Data is the url of the file).
     */
    suspend fun upload(file: File): Response<String>

    /**
     * Asynchronously delete file.
     *
     * @param filename The name of the file to delete (incl. extension).
     * @return A standardized response.
     */
    suspend fun delete(filename: String): Response<Unit>
}