package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrOpeningSeasonProductionLargeListItemBinding
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason.OpeningSeasonFcrListItemUiState

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OpeningSeasonFcrRecordLargeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: FfrOpeningSeasonProductionLargeListItemBinding =
        FfrOpeningSeasonProductionLargeListItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    var item: OpeningSeasonFcrListItemUiState? = null
        @ModelProp set(item) {
            if (field != item) {
                if (item != null) {
                    // binding.categoryName.text = item.categoryName
                }
                field = item
            }
        }

    var onAddNewFcrClickCallback: OnClickListener? = null
        @CallbackProp set

    var onViewFcrsClickCallback: OnClickListener? = null
        @CallbackProp set

    init {
        binding.cardView.setOnClickListener {
            // onViewFcrsClickCallback?.onClick(it)
        }
        binding.addNewProductionButton.setOnClickListener {
            onAddNewFcrClickCallback?.onClick(it)
        }
        binding.viewProductionsButton.setOnClickListener {
            onViewFcrsClickCallback?.onClick(it)
        }
    }
}