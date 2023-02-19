package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrSeasonSummarySubheaderViewBinding
import greenway_myanmar.org.util.kotlin.customViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SeasonSummarySubheaderView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private var binding = customViewBinding(FfrSeasonSummarySubheaderViewBinding::inflate)

    @JvmOverloads
    @ModelProp
    fun subheader(text: String = "") {
        bind(text)
    }

    fun bind(subheader: String) {
        binding.textView.text = subheader
    }

}
