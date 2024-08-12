/**
 * File Name: DetailsActivity.kt
 * Author: Malkit Kaur
 * Student ID: 200543614
 * Date: 2024-08-11
 * App Description: Handles the details view of a TodoItem, including updating and deleting the item.
 * Version: 1.0
 */

package ca.georgian.assignment4

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ca.georgian.assignment4.databinding.DetailsViewBinding
import java.util.*

/**
 * Activity class for displaying and editing the details of a TodoItem.
 */
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: DetailsViewBinding
    private val viewModel: TodoItemViewModel by viewModels()

    private lateinit var dataManager: DataManager

    private var todoItemId: String? = null
    private var selectedDate: Date? = null

    // Variables to store original data for comparison
    private var originalName: String? = null
    private var originalNotes: String? = null
    private var originalCompleted: Boolean = false
    private var originalDueDate: Date? = null
    private var originalHasDueDate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the DataManager
        dataManager = DataManager.instance()

        // Get the TodoItem ID from the intent
        todoItemId = intent.getStringExtra("todoItemId")

        // Load the TodoItem if ID is available, otherwise hide delete button
        if (todoItemId != null) {
            viewModel.loadTodoItemById(todoItemId!!)
        } else {
            binding.deleteButton.visibility = View.GONE
        }

        // Observe the TodoItem LiveData to update the UI and store original data
        viewModel.todoItem.observe(this) { todoItem ->
            todoItem?.let {
                binding.taskNameEditText.setText(it.name)
                binding.notesEditText.setText(it.notes)
                binding.switchCompletedDetail.isChecked = it.completed

                if (it.hasDueDate) {
                    binding.switchDateDetail.isChecked = true
                    binding.calendarView.visibility = View.VISIBLE
                    binding.calendarView.date = it.dueDate.time
                    selectedDate = it.dueDate
                } else {
                    binding.switchDateDetail.isChecked = false
                    binding.calendarView.visibility = View.GONE
                }

                // Store original data for change detection
                originalName = it.name
                originalNotes = it.notes
                originalCompleted = it.completed
                originalDueDate = it.dueDate
                originalHasDueDate = it.hasDueDate
            }
        }

        // Update selected date when calendar date changes
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.time
        }

        // Toggle calendar visibility based on switch state
        binding.switchDateDetail.setOnCheckedChangeListener { _, isChecked ->
            binding.calendarView.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Set up click listeners for the buttons
        binding.updateButton.setOnClickListener {
            showUpdateConfirmationDialog()
        }

        binding.deleteButton.setOnClickListener {
            deleteTodoItem()
        }

        binding.cancelButton.setOnClickListener {
            handleCancelAction()
        }
    }

    /**
     * Handles the cancel action by checking for changes and showing a discard dialog if necessary.
     */
    private fun handleCancelAction() {
        if (hasChanges()) {
            showDiscardChangesDialog()
        } else {
            finish()
        }
    }

    /**
     * Checks if any changes were made to the original data.
     *
     * @return True if changes are detected, false otherwise.
     */
    private fun hasChanges(): Boolean {
        val currentName = binding.taskNameEditText.text.toString()
        val currentNotes = binding.notesEditText.text.toString()
        val currentCompleted = binding.switchCompletedDetail.isChecked
        val currentDueDate = selectedDate
        val currentHasDueDate = binding.switchDateDetail.isChecked

        return currentName != originalName ||
                currentNotes != originalNotes ||
                currentCompleted != originalCompleted ||
                currentDueDate != originalDueDate ||
                currentHasDueDate != originalHasDueDate
    }

    /**
     * Shows a dialog to confirm discarding changes.
     */
    private fun showDiscardChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Discard Changes")
            .setMessage("You have unsaved changes. Are you sure you want to discard them?")
            .setPositiveButton("Yes") { _, _ ->
                finish() // Close activity and discard changes
            }
            .setNegativeButton("No", null)
            .show()
    }

    /**
     * Shows a dialog to confirm updating the TodoItem.
     */
    private fun showUpdateConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Update Todo Item")
            .setMessage("Are you sure you want to update this item?")
            .setPositiveButton("Yes") { _, _ ->
                saveTodoItem()
            }
            .setNegativeButton("No", null)
            .show()
    }

    /**
     * Saves the TodoItem with updated data.
     */
    private fun saveTodoItem() {
        val name = binding.taskNameEditText.text.toString()
        val notes = binding.notesEditText.text.toString()
        val hasDueDate = binding.switchDateDetail.isChecked
        val dueDate = if (hasDueDate) selectedDate else null
        val completed = binding.switchCompletedDetail.isChecked

        if (name.isNotEmpty()) {
            val todoItem = TodoItem(
                id = todoItemId ?: UUID.randomUUID().toString(),
                name = name,
                notes = notes,
                dueDate = dueDate ?: Date(),
                completed = completed,
                hasDueDate = hasDueDate
            )
            viewModel.saveTodoItem(todoItem)
            Toast.makeText(this, "Todo Item Updated", Toast.LENGTH_SHORT).show()

            // Navigate back to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        } else {
            Toast.makeText(this, "Please fill out the task name", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Deletes the TodoItem and navigates back to MainActivity.
     */
    private fun deleteTodoItem() {
        AlertDialog.Builder(this)
            .setTitle("Delete Todo Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.todoItem.value?.let { todoItem ->
                    viewModel.deleteTodoItem(todoItem)
                    Toast.makeText(this, "Todo Item Deleted", Toast.LENGTH_SHORT).show()

                    // Navigate back to the MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}

