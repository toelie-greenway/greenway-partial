package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrOpeningSeasonCategoryLargeListItemBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonCategoryListItemUiState
import greenway_myanmar.org.util.DateUtils

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

    var item: OpeningSeasonCategoryListItemUiState.CategoryItem? = null
        @ModelProp set(item) {
            if (field != item) {
                if (item != null) {
                    binding.categoryNameTextView.text = item.categoryName
                    binding.categoryExpenseTextView.setAmount(item.totalCategoryExpense)
                    binding.dateTextView.text =
                        item.lastRecordDate?.let { DateUtils.format(it, "MMMM d၊ yyyy") }.orEmpty()
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
            /* no-op */
        }
        binding.addNewExpenseButton.setOnClickListener {
            onAddNewExpenseClickCallback?.onClick(it)
        }
        binding.viewCategoryExpensesButton.setOnClickListener {
            onViewCategoryExpensesClickCallback?.onClick(it)
        }
    }
}