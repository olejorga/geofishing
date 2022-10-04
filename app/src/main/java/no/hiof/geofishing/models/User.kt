package no.hiof.geofishing.models

/**
 * Represents a "follow" relationship between two users.
 * @property id Unique identifier.
 * @property bio A short biography of the user.
 * @property name: The name of the user.
 * @property portrait Url to a photo of the user.
 */
data class User(
    val id: String,
    val bio: String,
    val name: String,
    val portrait: String
)