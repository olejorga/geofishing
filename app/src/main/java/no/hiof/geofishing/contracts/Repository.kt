package no.hiof.geofishing.contracts

import kotlinx.coroutines.flow.Flow

interface Repository<T> {
    fun create(data: T): Flow<Any>
    fun read(): Flow<List<T>>
    fun update(key: Any, data: T): Flow<Unit>
    fun delete(key: Any): Flow<Unit>
    fun find(key: Any): Flow<T>
}