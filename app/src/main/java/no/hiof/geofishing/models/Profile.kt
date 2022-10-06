package no.hiof.geofishing.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

/**
 * Represents a additional information about a user (one per auth account).
 * @property id Unique identifier (should the id of the auth account).
 * @property bio A short biography of the user.
 * @property name: The name of the user.
 * @property portrait Url to a photo of the user.
 * @property subscriptions A list of ids of users that this user follows.
 */
data class Profile(
    @Exclude
    @DocumentId
    val id: String? = null,
    val bio: String? = null,
    val name: String? = null,
    val portrait: String? = null,
    val subscriptions: List<String> = ArrayList()
)