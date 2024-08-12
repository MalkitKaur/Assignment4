import androidx.recyclerview.widget.RecyclerView
import ca.georgian.assignment4.TodoItem
import ca.georgian.assignment4.databinding.TodoItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * ViewHolder class for binding TodoItem data to the RecyclerView item views.
 *
 * @param binding The binding instance for the TodoItem layout.
 */
class TodoItemViewHolder(
    private val binding: TodoItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Binds the given TodoItem to the views in this ViewHolder.
     *
     * @param todoItem The TodoItem to be displayed in the RecyclerView.
     */
    fun bind(todoItem: TodoItem) {
        // Set the task name in the TextView
        binding.taskNameTextView.text = todoItem.name

        // Set the due date in the TextView, or display "No Due Date" if not set
        binding.dueDateTextView.text = if (todoItem.hasDueDate) {
            dateFormat.format(todoItem.dueDate)
        } else {
            "No Due Date"
        }

        // Set the checked state of the switch based on the completion status
        binding.switchCompleted.isChecked = todoItem.completed
    }
}
