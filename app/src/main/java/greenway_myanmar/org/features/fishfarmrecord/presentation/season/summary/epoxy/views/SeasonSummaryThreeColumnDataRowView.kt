package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import greenway_myanmar.org.databinding.FfrSeasonSummaryThreeColumnDataRowViewBinding
import greenway_myanmar.org.util.kotlin.customViewBinding

class SeasonSummaryThreeColumnDataRowView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryThreeColumnDataRowViewBinding::inflate)

    fun bind(item: Triple<String, String, String>, highlight: Boolean = false) {
        binding.firstTextView.text = item.first
        binding.secondTextView.text = item.second
        binding.thirdTextView.text = item.third
        binding.root.setBackgroundColor(
            if (highlight) {
                Color.parseColor("#F8F8F8")
            } else {
                Color.WHITE
            }
        )
    }

}
