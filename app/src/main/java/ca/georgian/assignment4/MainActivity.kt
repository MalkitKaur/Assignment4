package ca.georgian.assignment4

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgian.assignment4.databinding.ActivityMainBinding

/**
 * MainActivity class for displaying a list of Todo items.
 * Handles RecyclerView setup, data binding, and interaction with TodoItemViewModel.
 *
 * Author: Malkit Kaur
 * StudentID: 200543614
 * Date: 2024-08-11
 * App Description: Displays a list of Todo items and provides an option to add or view details of a Todo item.
 * Version: 1.0
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TodoItemViewModel by viewModels()

    private val adapter = TodoItemListAdapter { todoItem: TodoItem ->
        // Navigate to DetailsActivity when a Todo item is clicked
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("todoItemId", todoItem.id)
        }
        startActivity(intent)
    }

    /**
     * Called when the activity is first created. Initializes the RecyclerView and sets up observers.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with a LinearLayoutManager and the TodoItemListAdapter
        binding.firstRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.firstRecyclerView.adapter = adapter

        // Observe changes to the list of Todo items and update the adapter
        viewModel.todoItems.observe(this) { todoItems ->
            adapter.submitList(todoItems)
        }

        // Set up the button to add a new Todo item
        binding.addButton.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }

        // Load the initial list of Todo items
        viewModel.loadAllTodoItems()
    }

    /**
     * Called when the activity comes to the foreground. Refreshes the list of Todo items.
     */
    override fun onResume() {
        super.onResume()
        viewModel.loadAllTodoItems()
    }
}
