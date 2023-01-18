package greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory

class ExpenseCategoryPickerAdapter(private val itemClickCallback: ItemClickCallback) :
    ListAdapter<ExpenseCategoryPickerListItemUiState, ExpenseCategoryPickerViewHolder>(
        DiffItemCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseCategoryPickerViewHolder {
        return ExpenseCategoryPickerViewHolder(
            parent = parent,
            onItemClicked = {
                itemClickCallback.onItemClicked(it.category)
            }
        )
    }

    override fun onBindViewHolder(holder: ExpenseCategoryPickerViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    interface ItemClickCallback {
        fun onItemClicked(category: UiExpenseCategory)
    }

    object DiffItemCallback : DiffUtil.ItemCallback<ExpenseCategoryPickerListItemUiState>() {
        override fun areItemsTheSame(
            oldItem: ExpenseCategoryPickerListItemUiState,
            newItem: ExpenseCategoryPickerListItemUiState
        ): Boolean {
            return oldItem.category.id == newItem.category.id
        }

        override fun areContentsTheSame(
            oldItem: ExpenseCategoryPickerListItemUiState,
            newItem: ExpenseCategoryPickerListItemUiState
        ): Boolean {
            return oldItem == newItem
        }

    }

}
