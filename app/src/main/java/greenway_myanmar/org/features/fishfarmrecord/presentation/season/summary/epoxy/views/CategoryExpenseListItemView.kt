package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrSeasonSummaryCategoryExpenseListItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCategoryExpense
import greenway_myanmar.org.util.DateUtils
import greenway_myanmar.org.util.kotlin.customViewBinding
import kotlinx.datetime.toJavaInstant

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CategoryExpenseListItemView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryCategoryExpenseListItemViewBinding::inflate)

    @ModelProp
    fun item(item: UiCategoryExpense) {
        bind(item)
    }

    fun bind(item: UiCategoryExpense) {
        binding.dateTextView.text = DateUtils.format((item.lastRecordDate ?: kotlinx.datetime.Clock.System.now()).toJavaInstant(), "d MMM yyyy")
        binding.expenseTextView.setAmount(item.totalExpenses)
        binding.categoryNameTextView.text = item.category.name
    }
}
