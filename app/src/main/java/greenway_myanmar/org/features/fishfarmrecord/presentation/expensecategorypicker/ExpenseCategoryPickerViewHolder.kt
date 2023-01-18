package greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrExpenseCategoryPickerListItemBinding

class ExpenseCategoryPickerViewHolder(
    parent: ViewGroup,
    onItemClicked: (ExpenseCategoryPickerListItemUiState) -> Unit
) : ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_expense_category_picker_list_item, parent, false)
) {
    private val binding = FfrExpenseCategoryPickerListItemBinding.bind(itemView)
    private var _item: ExpenseCategoryPickerListItemUiState? = null

    init {
        binding.root.setOnClickListener {
            onItemClicked(onItemClicked)
        }
    }

    private fun onItemClicked(onItemClicked: (ExpenseCategoryPickerListItemUiState) -> Unit) {
        _item?.let { item ->
            onItemClicked(item)
        }
    }

    fun bind(item: ExpenseCategoryPickerListItemUiState) {
        _item = item
        binding.categoryNameTextView.text = item.category.name
        binding.root.isChecked = item.checked
    }
}