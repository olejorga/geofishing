package no.hiof.geofishing.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

/**
 * Represents a checklist (one per user).
 * @property id Unique identifier (should be the id of the user that owns the list).
 * @property items A list of checklist items.
 */
data class Checklist(
    @Exclude
    @DocumentId
    val id: String? = null,
    val items: List<ChecklistItem> = ArrayList()
)

/**
 * Represents a single checklist item (many per checklist).
 * @property completed Whether the item is crossed of or not.
 * @property description A description of the item.
 * @property reminder A time to remind the user to complete the item.
 */
data class ChecklistItem(
    val completed: Boolean = false,
    val description: String? = null,
    val reminder: Timestamp? = null
)
