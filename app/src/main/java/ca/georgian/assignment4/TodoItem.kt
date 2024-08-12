package ca.georgian.assignment4

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.Date

/**
 * Data class representing a Todo item.
 * This class is used for Firestore deserialization and holds information about a Todo item.
 *
 * Author: Malkit Kaur
 * StudentID: 200543614
 * Date: 2024-08-11
 * App Description: Represents a Todo item with its attributes for storing and retrieving from Firestore.
 * Version: 1.0
 */
@IgnoreExtraProperties
data class TodoItem(
    @DocumentId val id: String = "", // Unique identifier for the Todo item
    val name: String = "", // Name of the Todo item
    val notes: String = "", // Additional notes for the Todo item
    val dueDate: Date = Date(), // Due date for the Todo item
    val completed: Boolean = false, // Status indicating if the Todo item is completed
    val hasDueDate: Boolean = false // Flag indicating if the Todo item has a due date
) {
    // No-argument constructor required for Firestore deserialization
    constructor() : this("", "", "", Date(), false, false)
}
