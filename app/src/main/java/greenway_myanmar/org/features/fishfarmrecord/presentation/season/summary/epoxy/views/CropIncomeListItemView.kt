package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.FfrSeasonSummaryCropIncomeListItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCropIncome
import greenway_myanmar.org.util.DateUtils
import greenway_myanmar.org.util.kotlin.customViewBinding
import kotlinx.datetime.toJavaInstant

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CropIncomeListItemView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrSeasonSummaryCropIncomeListItemViewBinding::inflate)

    @ModelProp
    fun item(item: UiCropIncome) {
        bind(item)
    }

    fun bind(item: UiCropIncome) {
        binding.dateTextView.text = DateUtils.format(item.date.toJavaInstant(), "d MMM yyyy")
        binding.incomeTextView.setAmount(item.income)
        binding.cropNameTextView.text = item.crop.name
    }
}
