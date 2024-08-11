import androidx.recyclerview.widget.RecyclerView
import ca.georgian.assignment4.TodoItem
import ca.georgian.assignment4.databinding.TodoItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TodoItemViewHolder(
    private val binding: TodoItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun bind(todoItem: TodoItem) {
        binding.taskNameTextView.text = todoItem.name
        binding.dueDateTextView.text = if (todoItem.hasDueDate) dateFormat.format(todoItem.dueDate) else "No Due Date"
        binding.switchCompleted.isChecked = todoItem.completed
    }
}
