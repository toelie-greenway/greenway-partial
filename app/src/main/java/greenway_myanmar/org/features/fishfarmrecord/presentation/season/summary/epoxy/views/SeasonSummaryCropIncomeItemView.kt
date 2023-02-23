package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import greenway_myanmar.org.databinding.FfrSeasonSummaryCropIncomeItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCropIncome
import greenway_myanmar.org.util.kotlin.customViewBinding

class SeasonSummaryCropIncomeItemView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryCropIncomeItemViewBinding::inflate)

    fun bind(item: UiCropIncome) {
        binding.dateTextView.text = item.formattedDate
        binding.dataRowContainer.removeAllViews()
        binding.dataRowContainer.addView(
            SeasonSummaryTwoColumnDataRowView(context).apply {
                bind(
                    item = Pair(item.crop.name, item.formattedIncome(context)),
                    highlight = true
                )
            }
        )
    }

}
