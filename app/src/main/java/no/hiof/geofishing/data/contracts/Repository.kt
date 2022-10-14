package no.hiof.geofishing.data.contracts

import kotlinx.coroutines.flow.Flow

/**
 * Represents a repository (uses the "C-R-U-D" pattern).
 *
 * - All methods return a standardized response.
 * - All read methods return a flow (realtime data stream), within the standardized response.
 */
interface Repository<T> {

    /**
     * Asynchronously save an entity in the database.
     *
     * @param entity The entity to save.
     * @param id The ID the entity will have in the database (optional).
     * @return A standardized response (Data is the ID of newly stored entity).
     */
    suspend fun create(entity: T, id: String? = null): Response<String>

    /**
     * Create a realtime stream of entities from the database.
     *
     * @return A standardized response (Data is a list of all entities).
     */
    fun read(): Flow<Response<List<T>>>

    /**
     * Asynchronously update an entity in the database.
     *
     * @param id The ID of the entity in the database.
     * @param data An hash map that is a partial entity (properties included will be updated).
     * @return A standardized response (No data).
     */
    suspend fun update(id: String, data: Map<String, Any>): Response<Unit>

    /**
     * Asynchronously delete an entity in the database.
     *
     * @param id The ID of the entity in the database.
     * @return A standardized response (No data).
     */
    suspend fun delete(id: String): Response<Unit>

    /**
     * Create a realtime stream of the entity that has the matching id.
     *
     * @param id The target id.
     * @return A standardized response (Data is the entity with the matching id).
     */
    fun find(id: String): Flow<Response<T>>

    /**
     * Create a realtime stream of entities that has a property with a specific value.
     *
     * @param property The target property within the entity.
     * @param value The value of the targeted property.
     * @return A standardized response (Data is a list of matching entities).
     */
    fun search(property: String, value: Any): Flow<Response<List<T>>>
}