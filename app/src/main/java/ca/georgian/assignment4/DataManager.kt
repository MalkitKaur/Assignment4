package ca.georgian.assignment4

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DataManager private constructor() {
    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        private const val TAG = "DataManager"

        @Volatile
        private var m_instance: DataManager? = null

        fun instance(): DataManager {
            if (m_instance == null) {
                synchronized(this) {
                    if (m_instance == null) {
                        m_instance = DataManager()
                    }
                }
            }
            return m_instance!!
        }
    }

    // Function to insert a TodoItem in Firestore
    suspend fun insert(todoItem: TodoItem) {
        try {
            db.collection("todoItems").document(todoItem.id).set(todoItem).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting TodoItem: ${e.message}", e)
        }
    }

    // Function to update a TodoItem in Firestore
    suspend fun update(todoItem: TodoItem) {
        try {
            db.collection("todoItems").document(todoItem.id).set(todoItem).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating TodoItem: ${e.message}", e)
        }
    }

    // Function to delete a TodoItem from Firestore
    suspend fun delete(todoItem: TodoItem) {
        try {
            db.collection("todoItems").document(todoItem.id).delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting TodoItem: ${e.message}", e)
        }
    }

    // Function to get all TodoItems from Firestore
    suspend fun getAllTodoItems(): List<TodoItem> {
        return try {
            val result = db.collection("todoItems").get().await()
            result?.toObjects(TodoItem::class.java) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting TodoItems: ${e.message}", e)
            emptyList()
        }
    }

    // Function to get a TodoItem by ID from Firestore
    suspend fun getTodoItemById(id: String): TodoItem? {
        return try {
            val result = db.collection("todoItems").document(id).get().await()
            result?.toObject(TodoItem::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting TodoItem by ID: ${e.message}", e)
            null
        }
    }
}
