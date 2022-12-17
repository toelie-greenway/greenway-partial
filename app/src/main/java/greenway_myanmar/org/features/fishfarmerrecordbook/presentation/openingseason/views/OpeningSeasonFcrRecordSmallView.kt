package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrOpeningSeasonFcrRecordSmallListItemBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OpeningSeasonFcrRecordSmallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: FfrOpeningSeasonFcrRecordSmallListItemBinding =
        FfrOpeningSeasonFcrRecordSmallListItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    var onAddNewFcrClickCallback: OnClickListener? = null
        @CallbackProp set

    init {
        binding.cardView.setOnClickListener {
            onAddNewFcrClickCallback?.onClick(it)
        }
    }
}