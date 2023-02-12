package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import greenway_myanmar.org.databinding.FfrExpenseLineItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist.ExpenseLineItemUiState
import greenway_myanmar.org.util.kotlin.customViewBinding

class ExpenseLineItemView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private val binding = customViewBinding(FfrExpenseLineItemViewBinding::inflate)

    fun bind(item: ExpenseLineItemUiState, @ColorInt backgroundColor: Int) {
        binding.expenseNameTextView.text = item.name
        binding.expenseQuantityTextView.text = item.quantity
        binding.costTextView.setAmount(item.cost)
        binding.root.setBackgroundColor(backgroundColor)
    }
}
