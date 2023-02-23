package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFcrRecordListFishItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFcr
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.SeasonSummaryTwoColumnDataRowView
import greenway_myanmar.org.util.kotlin.customViewBinding

class FcrRecordListFishItemView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrFcrRecordListFishItemViewBinding::inflate)

    fun bind(item: UiFcr) {
        binding.fishNameTextView.text = item.fish.name
        binding.ratioTextView.text = resources.getString(
            R.string.ffr_fcr_label_formatted_ratio,
            numberFormat.format(item.ratio)
        )
        binding.dataRowContainer.removeAllViews()
        buildDataRowsFrom(item).forEachIndexed { index, lineItem ->
            binding.dataRowContainer.addView(
                SeasonSummaryTwoColumnDataRowView(context).apply {
                    bind(
                        item = lineItem,
                        highlight = index % 2 == 0
                    )
                }
            )
        }
    }

    private fun buildDataRowsFrom(fcr: UiFcr): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        list.add(
            Pair(
                resources.getString(R.string.ffr_fcr_label_total_feed_weight),
                resources.getString(
                    R.string.formatted_viss,
                    numberFormat.format(fcr.feedWeight)
                )
            )
        )
        list.add(
            Pair(
                resources.getString(R.string.ffr_fcr_label_total_gain_weight),
                resources.getString(
                    R.string.formatted_viss,
                    numberFormat.format(fcr.gainWeight)
                )
            )
        )
        return list
    }


}
