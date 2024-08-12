/*
 * File Name: DataManager.kt
 * Author: Malkit Kaur
 * Student ID: 200543614
 * Date: 2024-08-11
 * App Description: This class manages CRUD operations for TodoItems in Firestore using Firebase and Kotlin.
 * Version: 1.0
 */

package ca.georgian.assignment4

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * DataManager class is a singleton that provides functions to interact with the Firestore database.
 * It handles the creation, update, retrieval, and deletion of TodoItem objects in Firestore.
 */
class DataManager private constructor() {
    // Firebase Firestore database instance
    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        private const val TAG = "DataManager"

        // Volatile instance of DataManager to ensure thread safety
        @Volatile
        private var m_instance: DataManager? = null

        /**
         * Provides a thread-safe instance of DataManager.
         * @return Singleton instance of DataManager
         */
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

    /**
     * Inserts a TodoItem into Firestore.
     * @param todoItem The TodoItem to be inserted.
     */
    suspend fun insert(todoItem: TodoItem) {
        try {
            db.collection("todoItems").document(todoItem.id).set(todoItem).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting TodoItem: ${e.message}", e)
        }
    }

    /**
     * Updates an existing TodoItem in Firestore.
     * @param todoItem The TodoItem with updated values.
     */
    suspend fun update(todoItem: TodoItem) {
        try {
            db.collection("todoItems").document(todoItem.id).set(todoItem).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating TodoItem: ${e.message}", e)
        }
    }

    /**
     * Deletes a TodoItem from Firestore.
     * @param todoItem The TodoItem to be deleted.
     */
    suspend fun delete(todoItem: TodoItem) {
        try {
            db.collection("todoItems").document(todoItem.id).delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting TodoItem: ${e.message}", e)
        }
    }

    /**
     * Retrieves all TodoItems from Firestore.
     * @return List of all TodoItems or an empty list if an error occurs.
     */
    suspend fun getAllTodoItems(): List<TodoItem> {
        return try {
            val result = db.collection("todoItems").get().await()
            result?.toObjects(TodoItem::class.java) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting TodoItems: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Retrieves a specific TodoItem by its ID from Firestore.
     * @param id The ID of the TodoItem to retrieve.
     * @return The TodoItem if found, or null if an error occurs or the item is not found.
     */
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
