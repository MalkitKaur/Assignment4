package ca.georgian.assignment4

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgian.assignment4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TodoItemViewModel by viewModels()

    private val adapter = TodoItemListAdapter { todoItem: TodoItem ->
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("todoItemId", todoItem.id)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.firstRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.firstRecyclerView.adapter = adapter

        // Observe LiveData
        viewModel.todoItems.observe(this) { todoItems ->
            adapter.submitList(todoItems)
        }

        // Set up button to add a new Todo item
        binding.addButton.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }

        // Load initial data
        viewModel.loadAllTodoItems()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAllTodoItems()
    }
}
