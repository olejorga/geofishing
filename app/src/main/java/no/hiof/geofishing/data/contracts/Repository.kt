package no.hiof.geofishing.data.contracts

import kotlinx.coroutines.flow.Flow

/**
 * Represents a repository.
 *
 * - Uses basic a "C-R-U-D" pattern.
 * - Has an additional method "find", to return a single entity based on a key-value pair.
 * - All methods return a standardized response.
 * - All read methods return a flow (realtime data stream), within the standardized response.
 */
interface Repository<T> {
    suspend fun create(entity: T, id: String? = null): Response<String>
    fun read(): Flow<Response<List<T>>>
    suspend fun update(id: String, data: Map<String, Any>): Response<Unit>
    suspend fun delete(id: String): Response<Unit>
    fun find(key: String, value: Any): Flow<Response<List<T>>>
}