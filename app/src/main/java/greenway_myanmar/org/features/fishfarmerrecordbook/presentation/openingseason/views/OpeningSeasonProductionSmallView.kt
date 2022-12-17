package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrOpeningSeasonProductionSmallListItemBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OpeningSeasonProductionSmallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: FfrOpeningSeasonProductionSmallListItemBinding =
        FfrOpeningSeasonProductionSmallListItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    var onAddNewProductionClickCallback: OnClickListener? = null
        @CallbackProp set

    init {
        binding.cardView.setOnClickListener {
            onAddNewProductionClickCallback?.onClick(it)
        }
    }
}