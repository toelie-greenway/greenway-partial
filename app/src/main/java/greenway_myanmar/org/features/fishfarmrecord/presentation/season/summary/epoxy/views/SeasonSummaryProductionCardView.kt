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
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrSeasonSummaryProductionCardViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiProductionRecordSummary
import greenway_myanmar.org.util.UIUtils
import greenway_myanmar.org.util.kotlin.customViewBinding


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SeasonSummaryProductionCardView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryProductionCardViewBinding::inflate)

    @ModelProp
    fun item(summary: UiProductionRecordSummary) {
        binding.totalTextView.text = resources.getString(
            R.string.ffr_season_summary_label_formatted_total_production,
            numberFormat.format(summary.totalWeight),
            numberFormat.format(summary.totalIncome),
        )

        binding.listItemContainer.removeAllViews()
        summary.productionRecords.forEachIndexed { index, item ->
            if (index > 0) {
                binding.listItemContainer.addView(
                    createDivider()
                )
            }
            binding.listItemContainer.addView(
                SeasonSummaryProductionItemView(context).apply {
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
