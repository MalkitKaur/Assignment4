package ca.georgian.assignment4

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.georgian.assignment4.databinding.TodoItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Adapter class for displaying a list of Todo items in a RecyclerView.
 * It uses ListAdapter to handle list updates efficiently.
 *
 * Author: Malkit Kaur
 * StudentID: 200543614
 * Date: 2024-08-11
 * App Description: Binds TodoItem data to the RecyclerView and handles item clicks.
 * Version: 1.0
 */
class TodoItemListAdapter(private val onClick: (TodoItem) -> Unit) :
    ListAdapter<TodoItem, TodoItemListAdapter.TodoItemViewHolder>(TodoItemComparator) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Creates a new ViewHolder for Todo items.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new instance of TodoItemViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoItemViewHolder(binding, onClick, dateFormat)
    }

    /**
     * Binds a TodoItem to the specified ViewHolder.
     *
     * @param holder The ViewHolder to bind the TodoItem to.
     * @param position The position of the TodoItem in the list.
     */
    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder class for Todo items in the RecyclerView.
     *
     * @param binding The binding for the item view.
     * @param onClick Lambda function to handle item clicks.
     * @param dateFormat SimpleDateFormat instance for formatting dates.
     */
    class TodoItemViewHolder(
        private val binding: TodoItemBinding,
        private val onClick: (TodoItem) -> Unit,
        private val dateFormat: SimpleDateFormat
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds a TodoItem to the views in the ViewHolder.
         *
         * @param todoItem The TodoItem to be displayed.
         */
        fun bind(todoItem: TodoItem) {
            binding.taskNameTextView.text = todoItem.name

            // Display due date or current date if not available
            val dueDateToDisplay = todoItem.dueDate ?: Calendar.getInstance().time
            binding.dueDateTextView.text = dateFormat.format(dueDateToDisplay)

            // Update UI based on completion status
            if (todoItem.completed) {
                binding.taskNameTextView.paintFlags = binding.taskNameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.taskNameTextView.setTextColor(Color.GRAY)
            } else {
                binding.taskNameTextView.paintFlags = binding.taskNameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.taskNameTextView.setTextColor(Color.BLACK)
            }

            // Update Due Date color if overdue
            if (todoItem.hasDueDate && todoItem.dueDate!!.before(Calendar.getInstance().time)) {
                binding.dueDateTextView.setTextColor(Color.RED)
            } else {
                binding.dueDateTextView.setTextColor(Color.BLACK)
            }

            binding.switchCompleted.isChecked = todoItem.completed

            // Set up click listeners
            binding.editButton.setOnClickListener {
                val intent = Intent(binding.root.context, DetailsActivity::class.java)
                intent.putExtra("todoItemId", todoItem.id)
                binding.root.context.startActivity(intent)
            }

            binding.root.setOnClickListener {
                onClick(todoItem)
            }
        }
    }

    companion object {
        private val TodoItemComparator = object : DiffUtil.ItemCallback<TodoItem>() {
            /**
             * Checks if two Todo items represent the same item based on their IDs.
             *
             * @param oldItem The Todo item from the old list.
             * @param newItem The Todo item from the new list.
             * @return True if the items have the same ID, false otherwise.
             */
            override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Checks if the contents of two Todo items are the same.
             *
             * @param oldItem The Todo item from the old list.
             * @param newItem The Todo item from the new list.
             * @return True if the items have the same properties, false otherwise.
             */
            override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
