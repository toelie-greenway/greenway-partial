package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import greenway_myanmar.org.databinding.FfrSeasonSummaryProductionItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiProductionRecord
import greenway_myanmar.org.util.kotlin.customViewBinding

class SeasonSummaryProductionItemView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryProductionItemViewBinding::inflate)

    fun bind(item: UiProductionRecord) {
        binding.dateTextView.text = item.formattedDate
        binding.weightTextView.text = item.formattedTotalWeight(context)
        binding.priceTextView.text = item.formattedTotalPrice(context)
        binding.dataRowContainer.removeAllViews()
        item.productions.forEach {
            if (it.productionsPerFishSize.isNotEmpty()) {
                binding.dataRowContainer.addView(SeasonSummaryProductionFishItemView(context).apply {
                    bind(it)
                }
                )
            }
        }
    }
}
