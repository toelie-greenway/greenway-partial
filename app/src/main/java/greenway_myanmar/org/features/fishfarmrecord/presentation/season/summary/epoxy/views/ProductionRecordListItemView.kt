package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrSeasonSummaryProductionListItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiProductionRecord
import greenway_myanmar.org.util.DateUtils
import greenway_myanmar.org.util.kotlin.customViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ProductionRecordListItemView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryProductionListItemViewBinding::inflate)

    @ModelProp
    fun item(item: UiProductionRecord) {
        bind(item)
    }

    private fun bind(item: UiProductionRecord) {
        binding.dateTextView.text = DateUtils.format(item.date, "d MMM yyyy")
        binding.totalProductionTextView.text = context.resources.getString(
            R.string.formatted_viss, numberFormat.format(item.totalWeight)
        )
        binding.noteTextView.text = item.note
        binding.noteTextView.isVisible = item.note.isNotEmpty()
    }
}
