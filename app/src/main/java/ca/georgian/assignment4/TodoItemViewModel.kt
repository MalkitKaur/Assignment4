package ca.georgian.assignment4

import androidx.lifecycle.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing TodoItem data and communication between the UI and the DataManager.
 *
 * Author: Malkit Kaur
 * StudentID: 200543614
 * Date: 2024-08-11
 * App Description: Manages the data and operations for TodoItems, including loading, saving, updating, and deleting.
 * Version: 1.0
 */
class TodoItemViewModel : ViewModel() {

    // Alias for the DataManager instance
    private val dataManager = DataManager.instance()

    // LiveData to hold the list of TodoItems
    private val m_todoItems = MutableLiveData<List<TodoItem>>()
    val todoItems: LiveData<List<TodoItem>> get() = m_todoItems

    // LiveData to hold the selected TodoItem
    private val m_todoItem = MutableLiveData<TodoItem?>()
    val todoItem: LiveData<TodoItem?> get() = m_todoItem

    /**
     * Loads all TodoItems from the DataManager and updates LiveData.
     */
    fun loadAllTodoItems() {
        viewModelScope.launch {
            m_todoItems.value = dataManager.getAllTodoItems()
        }
    }

    /**
     * Loads a specific TodoItem by ID from the DataManager and updates LiveData.
     *
     * @param id The ID of the TodoItem to be loaded.
     */
    fun loadTodoItemById(id: String) {
        viewModelScope.launch {
            m_todoItem.value = dataManager.getTodoItemById(id)
        }
    }

    /**
     * Saves or updates a TodoItem in the DataManager.
     * If the TodoItem has an empty ID, it will be inserted; otherwise, it will be updated.
     * After saving or updating, all TodoItems are reloaded.
     *
     * @param todoItem The TodoItem to be saved or updated.
     */
    fun saveTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            if (todoItem.id.isEmpty()) {
                dataManager.insert(todoItem)
            } else {
                dataManager.update(todoItem)
            }
            loadAllTodoItems()
        }
    }

    /**
     * Deletes a TodoItem from the DataManager and reloads all TodoItems.
     *
     * @param todoItem The TodoItem to be deleted.
     */
    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            dataManager.delete(todoItem)
            loadAllTodoItems()
        }
    }
}
