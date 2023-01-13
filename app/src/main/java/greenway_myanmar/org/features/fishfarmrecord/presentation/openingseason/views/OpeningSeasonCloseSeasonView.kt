package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrOpeningSeasonCloseSeasonListItemBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OpeningSeasonCloseSeasonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: FfrOpeningSeasonCloseSeasonListItemBinding =
        FfrOpeningSeasonCloseSeasonListItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    var onCloseSeasonClickCallback: OnClickListener? = null
        @CallbackProp set

    init {
        binding.cardView.setOnClickListener {
            onCloseSeasonClickCallback?.onClick(it)
        }
    }
}