package no.hiof.geofishing.models

/**
 * Represents a checklist (one per user).
 * @property id owners id (user).
 * @property items A list of checklist items.
 */
data class Checklist(
    val id: String,
    val items: List<ChecklistItem>
)

/**
 * Represents a single checklist item (many per checklist).
 * @property completed Whether the item is crossed of or not.
 * @property description A description of the item.
 * @property reminder A time to remind the user to complete the item.
 */
data class ChecklistItem(
    val completed: Boolean = false,
    val description: String,
    val reminder: String? = null
)
