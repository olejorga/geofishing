package no.hiof.geofishing.models

/**
 * Represents a "follow" relationship between two users.
 * @property id Unique identifier.
 * @property subscriber A reference to the user following (usually id).
 * @property user A reference to the user being followed (usually id).
 */
data class Subscription(
    val id: String,
    val subscriber: String,
    val user: String
)
