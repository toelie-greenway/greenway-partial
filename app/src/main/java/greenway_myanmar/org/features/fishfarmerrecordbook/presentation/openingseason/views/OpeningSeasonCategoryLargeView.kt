package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrOpeningSeasonCategoryLargeListItemBinding
import greenway_myanmar.org.databinding.FfrOpeningSeasonCategorySmallListItemBinding
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason.OpeningSeasonCategoryListItemUiState

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OpeningSeasonCategoryLargeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: FfrOpeningSeasonCategoryLargeListItemBinding =
        FfrOpeningSeasonCategoryLargeListItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    var item: OpeningSeasonCategoryListItemUiState? = null
        @ModelProp set(item) {
            if (field != item) {
                if (item != null) {
                    binding.categoryName.text = item.categoryName
                }
                field = item
            }
        }

    var onAddNewExpenseClickCallback: OnClickListener? = null
        @CallbackProp set

    var onViewCategoryExpensesClickCallback: OnClickListener? = null
        @CallbackProp set

    init {
        binding.cardView.setOnClickListener {
            // onAddNewExpenseClickCallback?.onClick(it)
        }
        binding.addNewExpenseButton.setOnClickListener {
            onAddNewExpenseClickCallback?.onClick(it)
        }
        binding.viewCategoryExpensesButton.setOnClickListener {
            onViewCategoryExpensesClickCallback?.onClick(it)
        }
    }
}