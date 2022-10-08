package no.hiof.geofishing.data.contracts

/**
 * Represents a standardized response, that shall always be returned from the data layer.
 *
 * @property data The retrieved data (usually entities) (optional).
 * @property error A error message for when "shit hits the fan" (null if successful).
 */
data class Response<T>(
    val data: T? = null,
    val error: String? = null
)