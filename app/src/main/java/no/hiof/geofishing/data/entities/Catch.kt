package no.hiof.geofishing.data.entities

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import no.hiof.geofishing.data.constants.Species
import java.util.*

/**
 * Represents a catch.
 *
 * @property id Unique identifier (should be auto-generated by database).
 * @property created When the catch was created (should be auto-generated by database).
 * @property length The length of the fish caught.
 * @property latitude Latitude and longitude for where the fish was caught (defaults to now).
 * @property lure What type of lure was used to catch the fish.
 * @property longitude Latitude and longitude for where the fish was caught (defaults to now).
 * @property picture A reference to a photo of the catch (usually url).
 * @property place The general area where the fish was caught (should be filled based on location).
 * @property rod What type of rod was used to catch the fish.
 * @property species An enum that identifies the specie of fish.
 * @property title An appropriate title for the post.
 * @property description An appropriate description for the post.
 * @property profile A reference to the profile of the user that posted the catch (usually id).
 * @property weight The weight of the fish caught.
 */
data class Catch(
    @Exclude
    @DocumentId
    val id: String? = null,
    @ServerTimestamp
    val created: Date? = null,
    val description: String? = null,
    val latitude: Double? = null,
    val length: Int? = null,
    val longitude: Double? = null,
    val lure: String? = null,
    val picture: String? = null,
    val place: String? = null,
    val rod: String? = null,
    val species: String? = null,
    val title: String? = null,
    val profile: String? = null,
    val weight: Int? = null
)
