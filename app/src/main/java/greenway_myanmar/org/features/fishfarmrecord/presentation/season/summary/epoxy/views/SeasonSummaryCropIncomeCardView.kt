package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrSeasonSummaryCropIncomeCardViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCropIncomeSummary
import greenway_myanmar.org.util.UIUtils
import greenway_myanmar.org.util.kotlin.customViewBinding


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SeasonSummaryCropIncomeCardView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryCropIncomeCardViewBinding::inflate)

    @ModelProp
    fun item(summary: UiCropIncomeSummary) {
        binding.totalTextView.setAmount(summary.totalIncome)

        binding.listItemContainer.removeAllViews()
        summary.cropIncomes.forEachIndexed { index, item ->
            if (index > 0) {
                binding.listItemContainer.addView(
                    createDivider()
                )
            }
            binding.listItemContainer.addView(
                SeasonSummaryCropIncomeItemView(context).apply {
                    bind(item)
                }
            )
        }
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