package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrSeasonSummaryExpandToggleViewBinding
import greenway_myanmar.org.util.kotlin.customViewMergeBinding

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class SeasonSummaryExpandToggleView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewMergeBinding(FfrSeasonSummaryExpandToggleViewBinding::inflate)

    init {
        updateUi()
    }

    @CallbackProp
    fun clickListener(listener: OnClickListener?) {
        binding.button.setOnClickListener(listener)
    }

    @ModelProp
    fun expand(expand: Boolean) {
        updateUi(expand)
    }

    private fun updateUi(expanded: Boolean = false) {
        binding.button.setText(
            if (expanded) {
                R.string.ffr_season_summary_button_text_shrink
            } else {
                R.string.ffr_season_summary_button_text_expand
            }
        )
        binding.button.setIconResource(
            if (expanded) {
                R.drawable.ic_expand_less_black_24dp
            } else {
                R.drawable.ic_expand_more_black_24dp
            }
        )
    }
}
