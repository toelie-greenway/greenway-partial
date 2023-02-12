package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrExpenseListItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpense

class ExpenseListAdapter :
    ListAdapter<UiExpense, ExpenseListItemViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseListItemViewHolder {
        return ExpenseListItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ExpenseListItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }


    object ItemDiffCallback : ItemCallback<UiExpense>() {
        override fun areItemsTheSame(oldItem: UiExpense, newItem: UiExpense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UiExpense, newItem: UiExpense): Boolean {
            return oldItem == newItem
        }

    }
}

class ExpenseListItemViewHolder(parent: ViewGroup) : ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_expense_list_item_view, parent, false)
) {
    private val binding = FfrExpenseListItemViewBinding.bind(itemView)

    fun bind(item: UiExpense) {
        binding.root.bind(item)
    }
}