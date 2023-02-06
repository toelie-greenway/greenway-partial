package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.FfrExpenseCategoryInputViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory

class ExpenseCategoryInputView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var _mandatory = false

    private var _clickCallback: ClickCallback? = null

    var binding: FfrExpenseCategoryInputViewBinding =
        FfrExpenseCategoryInputViewBinding.inflate(
            LayoutInflater.from(context), this, true
        ).apply {
            container.setOnClickListener { _clickCallback?.onClick() }
        }

    var category: UiExpenseCategory? = null
        set(value) {
            if (field != value) {
                field = value
                bindData(value)
            }
        }

    private fun bindData(category: UiExpenseCategory?) {
        binding.categoryName.text = category?.name
            ?: context.resources.getString(R.string.ffr_add_edit_expense_placeholder_expense_category)
    }

    fun setClickCall(callback: ClickCallback) {
        _clickCallback = callback
    }

    fun setError(error: Text?) {
        binding.errorTextView.isVisible = error != null
        binding.errorTextView.text = error?.asString(context).orEmpty()
    }

    interface ClickCallback {
        fun onClick()
    }

}
