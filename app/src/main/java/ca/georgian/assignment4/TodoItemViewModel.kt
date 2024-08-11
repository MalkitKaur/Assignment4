package ca.georgian.assignment4

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class TodoItemViewModel : ViewModel() {

    // Alias for the DataManager instance
    private val dataManager = DataManager.instance()

    // LiveData to hold the list of TodoItems
    private val m_todoItems = MutableLiveData<List<TodoItem>>()
    val todoItems: LiveData<List<TodoItem>> get() = m_todoItems

    // LiveData to hold the selected TodoItem
    private val m_todoItem = MutableLiveData<TodoItem?>()
    val todoItem: LiveData<TodoItem?> get() = m_todoItem

    // Function to load all TodoItems from the DataManager
    fun loadAllTodoItems() {
        viewModelScope.launch {
            m_todoItems.value = dataManager.getAllTodoItems()
        }
    }

    // Function to load a specific TodoItem by ID from the DataManager
    fun loadTodoItemById(id: String) {
        viewModelScope.launch {
            m_todoItem.value = dataManager.getTodoItemById(id)
        }
    }

    // Function to save or update a TodoItem in the DataManager
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

    // Function to delete a TodoItem from the DataManager
    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            dataManager.delete(todoItem)
            loadAllTodoItems()
        }
    }
}
