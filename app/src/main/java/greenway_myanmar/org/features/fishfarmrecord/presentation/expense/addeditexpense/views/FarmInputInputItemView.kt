package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFarmInputInputItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.util.extensions.load

class FarmInputInputItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var _item: UiFarmInputCost? = null

    private val binding = FfrFarmInputInputItemViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun init(
        onRemoveClicked: (item: UiFarmInputCost) -> Unit
    ) {
        binding.removeButton.setOnClickListener {
            _item?.let {
                onRemoveClicked(it)
            }
        }
    }

    fun bind(item: UiFarmInputCost) {
        if (_item == item) {
            return
        }
        _item = item
        binding.productName.text = item.productName
        binding.productCost.text = resources.getString(
            R.string.ffr_add_edit_expense_formatted_farm_input_product_cost,
            numberFormat.format(item.amount),
            item.unit,
            numberFormat.format(item.totalCost)
        )
        binding.productIcon.load(context, item.productThumbnail)
    }
}