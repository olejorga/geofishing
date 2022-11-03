package no.hiof.geofishing.data.contracts

/**
 * Represents a standardized response, that shall always be returned from the data layer.
 *
 * @property data The retrieved data (usually entities) (optional).
 * @property error A error message for when "shit hits the fan" (null if successful).
 */
data class Response<T>(
    val data: T? = null,
    var error: String? = null
)

// sealed class Response<T> {
//     data class Success<T>(val data: T): Result<T>()
//     data class Error<T>(val message: String): Result<T>()
// }