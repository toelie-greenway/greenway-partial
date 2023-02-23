package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import greenway_myanmar.org.databinding.FfrSeasonSummaryProductionFishItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiProductionPerFish
import greenway_myanmar.org.util.kotlin.customViewBinding

class SeasonSummaryProductionFishItemView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryProductionFishItemViewBinding::inflate)

    fun bind(item: UiProductionPerFish) {
        binding.fishNameTextView.text = item.fish.name
        binding.dataRowContainer.removeAllViews()
        item.productionsPerFishSize.forEachIndexed { index, perFishSize ->
            binding.dataRowContainer.addView(
                SeasonSummaryThreeColumnDataRowView(context).apply {
                    bind(
                        item = Triple(
                            first = resources.getString(perFishSize.fishSize.labelResId),
                            second = perFishSize.formattedWeight(context),
                            third = perFishSize.formattedPrice(context)
                        ),
                        highlight = index % 2 == 0
                    )
                }
            )
        }
    }

}
