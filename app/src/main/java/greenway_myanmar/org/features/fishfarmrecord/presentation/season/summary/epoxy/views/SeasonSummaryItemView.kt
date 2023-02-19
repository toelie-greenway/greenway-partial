package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import greenway_myanmar.org.databinding.FfrSeasonSummaryItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.SeasonSummaryItemUiState
import greenway_myanmar.org.util.kotlin.customViewBinding

class SeasonSummaryItemView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var binding = customViewBinding(FfrSeasonSummaryItemViewBinding::inflate)

    fun bind(item: SeasonSummaryItemUiState) {
        binding.labelTextView.text = item.label
        binding.valueTextView.text = item.value
        if (item.valueTextColorResId != null) {
            binding.valueTextView.setTextColor(
                ContextCompat.getColor(context, item.valueTextColorResId)
            )
        }
    }

}
