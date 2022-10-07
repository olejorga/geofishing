package no.hiof.geofishing.data.entities

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

/**
 * Represents a additional information about a user (one per auth account).
 * @property id Unique identifier (should the id of the auth account).
 * @property bio A short biography of the user.
 * @property name: The name of the user.
 * @property portrait A reference to a photo of the user (usually url).
 */
data class Profile(
    @Exclude
    @DocumentId
    val id: String? = null,
    val bio: String? = null,
    val name: String? = null,
    val portrait: String? = null
)