package no.hiof.geofishing.models

/**
 * Represents a catch.
 * @property id Unique identifier.
 * @property created When the catch was created (defaults to now).
 * @property length The length of the fish caught.
 * @property location Latitude and longitude for where the fish was caught (defaults to now).
 * @property lure What type of lure was used to catch the fish.
 * @property picture Url to a photo of the catch.
 * @property place The general area where the fish was caught (should be filled based on location).
 * @property rod What type of rod was used to catch the fish.
 * @property species A reference to the specie of fish (usually id).
 * @property title An appropriate title for the post.
 * @property description An appropriate description for the post.
 * @property user A reference to the user that posted the catch (usually id).
 */
data class Catch(
    val id: String? = null,
    val created: String? = null,
    val description: String? = null,
    val length: Int,
    val location: List<Double>? = null,
    val lure: String? = null,
    val picture: String,
    val place: String,
    val rod: String? = null,
    val species: String,
    val title: String,
    val user: String,
    val weight: Int
)
