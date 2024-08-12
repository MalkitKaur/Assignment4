package ca.georgian.assignment4

import androidx.recyclerview.widget.DiffUtil

/**
 * Comparator class for calculating differences between two lists of TodoItem.
 * This is used by the RecyclerView Adapter to efficiently update its list.
 *
 * Author: Malkit Kaur
 * StudentID: 200543614
 * Date: 2024-08-11
 * App Description: Provides a mechanism to compute the difference between old and new lists of Todo items
 * and determine whether items or their contents have changed.
 * Version: 1.0
 */
class TodoItemComparator : DiffUtil.ItemCallback<TodoItem>() {

    /**
     * Checks if two Todo items represent the same item.
     * This method is used to determine if two items have the same identity.
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
     * This method is used to determine if the properties of two items are identical.
     *
     * @param oldItem The Todo item from the old list.
     * @param newItem The Todo item from the new list.
     * @return True if the items have the same properties, false otherwise.
     */
    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}
