package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.databinding.FfrExpenseListItemCardViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist.ExpenseLineItemUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpense
import greenway_myanmar.org.util.DateUtils
import greenway_myanmar.org.util.extensions.load
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.kotlin.customViewBinding
import kotlinx.datetime.toJavaInstant
import java.math.BigDecimal

class ExpenseListItemCardView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrExpenseListItemCardViewBinding::inflate)

    private var onRemarkClick: (remark: String?, imageUrl: String?) -> Unit = { _, _ -> }

    fun init(
        onRemarkClick: (remark: String?, imageUrl: String?) -> Unit
    ) {
        this.onRemarkClick = onRemarkClick
    }

    fun bind(item: UiExpense) {
        binding.dateTextView.text = DateUtils.format(item.date.toJavaInstant(), "MMM d၊ yyyy · EEE")
        binding.totalCostTextView.setAmount(item.totalCost.orZero())
        bindRemark(item)

        binding.listItemContainer.removeAllViews()
        buildExpenseLinesFrom(item).forEachIndexed { index, lineItem ->
            binding.listItemContainer.addView(
                ExpenseLineItemView(context)
                    .apply {
                        bind(lineItem, getBackgroundColor(index))
                    }
            )
        }
    }

    private fun bindRemark(item: UiExpense) {
        binding.remarkImageView.isVisible = item.hasRemarkPhoto()
        binding.expandImageButton.isVisible = item.hasRemarkPhoto()
        item.photos?.firstOrNull()?.let {
            binding.remarkImageView.load(context, it)
        }
        binding.remarkImageMarginEnd.isVisible = item.hasRemarkPhoto()

        binding.remarkTextView.text = item.remark.orEmpty()
        binding.remarkTextView.isVisible = item.hasRemarkText()

        binding.remarkGroup.isVisible = item.hasRemark()
    }


    private fun buildExpenseLinesFrom(expense: UiExpense): List<ExpenseLineItemUiState> {
        val list = mutableListOf<ExpenseLineItemUiState>()

        expense.familyQuantity?.let { familyQuantity ->
            if (familyQuantity > 0) {
                list.add(
                    ExpenseLineItemUiState(
                        name = "မိသားစုဝင်",
                        quantity = numberFormat.format(familyQuantity) + " ယောက်",
                        cost = expense.familyCost.orZero()
                    )
                )
            }
        }

        expense.labourQuantity?.let { labourQuantity ->
            if (labourQuantity > 0) {
                list.add(
                    ExpenseLineItemUiState(
                        name = "လူ",
                        quantity = numberFormat.format(labourQuantity) + " ယောက်",
                        cost = expense.labourCost.orZero()
                    )
                )
            }
        }

        expense.machineryCost?.let { machineryCost ->
            if (machineryCost > BigDecimal.ZERO) {
                list.add(
                    ExpenseLineItemUiState(
                        name = "စက်ပစ္စည်း",
                        quantity = "",
                        cost = machineryCost
                    )
                )
            }
        }

        expense.inputs?.forEach { farmInput ->
            list.add(
                ExpenseLineItemUiState(
                    name = farmInput.productName,
                    quantity = numberFormat.format(farmInput.amount) + " " + farmInput.unit,
                    cost = farmInput.totalCost
                ),
            )
        }

        if (expense.generalExpense != null && expense.generalExpenseCategory != null) {
            list.add(
                ExpenseLineItemUiState(
                    name = expense.generalExpenseCategory.name,
                    quantity = "",
                    cost = expense.generalExpense
                ),
            )
        }

        return list
    }

    @ColorInt
    fun getBackgroundColor(index: Int): Int {
        return if (index % 2 == 0) {
            Color.parseColor("#99EFEFF4")
        } else {
            Color.parseColor("#FFFFFFFF")
        }
    }
}