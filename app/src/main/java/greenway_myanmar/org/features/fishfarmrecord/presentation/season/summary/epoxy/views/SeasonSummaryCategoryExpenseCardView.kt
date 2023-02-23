package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrSeasonSummaryCategoryExpenseCardViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCategoryExpense
import greenway_myanmar.org.util.DateUtils
import greenway_myanmar.org.util.UIUtils
import greenway_myanmar.org.util.kotlin.customViewBinding
import kotlinx.datetime.toJavaInstant

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SeasonSummaryCategoryExpenseCardView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryCategoryExpenseCardViewBinding::inflate)

    @CallbackProp
    fun clickListener(listener: OnClickListener?) {
        binding.root.setOnClickListener(listener)
    }

    @ModelProp
    fun expand(expand: Boolean) {
        expandShrink(expand)
    }

    private fun expandShrink(expand: Boolean) {
        binding.lastRecordDateTextView.isVisible = !expand
        binding.listItemContainer.isVisible = expand
    }

    @ModelProp
    fun item(item: UiCategoryExpense) {
        bind(item)
    }

    fun bind(item: UiCategoryExpense) {
        binding.categoryNameTextView.text = item.category.name
        binding.expenseTextView.setAmount(item.totalExpenses)
        binding.listItemContainer.removeAllViews()
        item.expenses.forEachIndexed { index, expense ->
            if (index > 0) {
                binding.listItemContainer.addView(
                    createDivider()
                )
            }
            binding.listItemContainer.addView(
                SeasonSummaryExpenseItemView(context).apply {
                    bind(expense)
                }
            )
        }
        binding.lastRecordDateTextView.text = DateUtils.format(
            (item.lastRecordDate ?: kotlinx.datetime.Clock.System.now()).toJavaInstant(),
            "d MMM yyyy"
        )
    }

    private fun createDivider(): View {
        val divider = View(context)
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            UIUtils.dpToPx(context, 1)
        )
        divider.layoutParams = lp
        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.app_divider))
        return divider
    }
}
