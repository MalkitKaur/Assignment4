package ca.georgian.assignment4

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.Date

@IgnoreExtraProperties
data class TodoItem(
    @DocumentId val id: String = "",
    val name: String = "",
    val notes: String = "",
    val dueDate: Date = Date(),
    val completed: Boolean = false,
    val hasDueDate: Boolean = false
) {
    // No-argument constructor required for Firestore deserialization
    constructor() : this("", "", "", Date(), false, false)
}
