package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import greenway_myanmar.org.databinding.FfrSeasonSummaryTwoColumnDataRowViewBinding
import greenway_myanmar.org.util.kotlin.customViewBinding

class SeasonSummaryTwoColumnDataRowView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryTwoColumnDataRowViewBinding::inflate)

    fun bind(item: Pair<String, String>, highlight: Boolean = false) {
        binding.firstTextView.text = item.first
        binding.secondTextView.text = item.second
        binding.root.setBackgroundColor(
            if (highlight) {
                Color.parseColor("#F8F8F8")
            } else {
                Color.WHITE
            }
        )
    }

}
