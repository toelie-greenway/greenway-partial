package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrSeasonSummaryExpenseItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpense
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.kotlin.customViewBinding
import java.math.BigDecimal

class SeasonSummaryExpenseItemView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryExpenseItemViewBinding::inflate)

    fun bind(item: UiExpense) {
        binding.dateTextView.text = item.formattedDate
        binding.dataRowContainer.removeAllViews()
        buildExpenseLinesFrom(item).forEachIndexed { index, lineItem ->
            binding.dataRowContainer.addView(
                SeasonSummaryThreeColumnDataRowView(context).apply {
                    bind(
                        item = lineItem,
                        highlight = index % 2 == 0
                    )
                }
            )
        }
    }

    private fun buildExpenseLinesFrom(expense: UiExpense): List<Triple<String, String, String>> {
        val list = mutableListOf<Triple<String, String, String>>()

        expense.familyQuantity?.let { familyQuantity ->
            if (familyQuantity > 0) {
                list.add(
                    Triple(
                        first = "မိသားစုဝင်",
                        second = numberFormat.format(familyQuantity) + " ယောက်",
                        third =
                        resources.getString(
                            R.string.formatted_kyat,
                            numberFormat.format(expense.familyCost)
                        )
                    )
                )
            }
        }

        expense.labourQuantity?.let { labourQuantity ->
            if (labourQuantity > 0) {
                list.add(
                    Triple(
                        first = "လူ",
                        second = numberFormat.format(labourQuantity) + " ယောက်",
                        third = resources.getString(
                            R.string.formatted_kyat,
                            numberFormat.format(expense.labourCost.orZero())
                        )
                    )
                )
            }
        }

        expense.machineryCost?.let { machineryCost ->
            if (machineryCost > BigDecimal.ZERO) {
                list.add(
                    Triple(
                        first = "စက်ပစ္စည်း",
                        second = "",
                        third = resources.getString(
                            R.string.formatted_kyat,
                            numberFormat.format(machineryCost)
                        )
                    )
                )
            }
        }

        expense.inputs?.forEach { farmInput ->
            list.add(
                Triple(
                    first = farmInput.productName,
                    second = numberFormat.format(farmInput.amount) + " " + farmInput.unit,
                    third = resources.getString(
                        R.string.formatted_kyat,
                        numberFormat.format(farmInput.totalCost)
                    )
                ),
            )
        }

        if (expense.generalExpense != null && expense.generalExpenseCategory != null) {
            list.add(
                Triple(
                    first = expense.generalExpenseCategory.name,
                    second = "",
                    third = resources.getString(
                        R.string.formatted_kyat,
                        numberFormat.format(expense.generalExpense)
                    )
                ),
            )
        }

        return list
    }
}
