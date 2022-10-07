package no.hiof.geofishing.data.contracts

data class Response<T>(
    val data: T? = null,
    val error: String? = null
)