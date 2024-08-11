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

class TodoItemListAdapter(private val onClick: (TodoItem) -> Unit) :
    ListAdapter<TodoItem, TodoItemListAdapter.TodoItemViewHolder>(TodoItemComparator) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoItemViewHolder(binding, onClick, dateFormat)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TodoItemViewHolder(
        private val binding: TodoItemBinding,
        private val onClick: (TodoItem) -> Unit,
        private val dateFormat: SimpleDateFormat
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoItem: TodoItem) {
            binding.taskNameTextView.text = todoItem.name

            // Check if due date exists, otherwise use current date
            val dueDateToDisplay = todoItem.dueDate ?: Calendar.getInstance().time
            binding.dueDateTextView.text = dateFormat.format(dueDateToDisplay)

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
            override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
