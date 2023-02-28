package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import greenway_myanmar.org.databinding.FfrSeasonSummaryFcrRecordItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list.views.FcrRecordListFishItemView
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFcrRecord
import greenway_myanmar.org.util.kotlin.customViewBinding

class SeasonSummaryFcrRecordItemView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryFcrRecordItemViewBinding::inflate)

    fun bind(item: UiFcrRecord) {
        binding.dateTextView.text = item.formattedDate
        binding.dataRowContainer.removeAllViews()
        item.ratios.forEach {
            binding.dataRowContainer.addView(
                FcrRecordListFishItemView(context).apply {
                    this.bind(it)
                }
            )
        }
    }

}
