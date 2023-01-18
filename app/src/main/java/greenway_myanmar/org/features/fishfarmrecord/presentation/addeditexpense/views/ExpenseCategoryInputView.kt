package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import greenway_myanmar.org.R
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
//
//  override fun setMandatory(mandatory: Boolean) {
//    _mandatory = mandatory
//  }
//
//  override fun isMandatory(): Boolean = _mandatory
//
//  override fun getValue(): AsymtExpenseCategory? {
//    return category
//  }
//
//  override fun validate(): Boolean {
//    return if (_mandatory && category == null) {
//      showError()
//      false
//    } else {
//      clearError()
//      true
//    }
//  }
//
//  override fun isEmpty(): Boolean {
//    return category != null
//  }
//
//  override fun showError() {
//    binding.errorTextView.visibility = View.VISIBLE
//  }
//
//  override fun clearError() {
//    binding.errorTextView.visibility = View.GONE
//  }

    interface ClickCallback {
        fun onClick()
    }

}
