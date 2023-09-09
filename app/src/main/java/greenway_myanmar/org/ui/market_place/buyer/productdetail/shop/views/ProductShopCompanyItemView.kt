package greenway_myanmar.org.ui.market_place.buyer.productdetail.shop.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.ProductShopComapnyItemViewBinding
import greenway_myanmar.org.ui.common.OnCompanyItemClickListener
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.extensions.load
import greenway_myanmar.org.util.kotlin.customViewMergeBinding
import greenway_myanmar.org.vo.marketplace.Company


class ProductShopCompanyItemView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    private val binding = customViewMergeBinding(ProductShopComapnyItemViewBinding::inflate)

    var company: Company? = null
        set(value) {
            if (field != null && field == value) {
                return
            }
            field = value
            bind()
        }

    var clickListener: OnCompanyItemClickListener? = null

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        val outValue = TypedValue()
        getContext().theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        background = ContextCompat.getDrawable(context, outValue.resourceId)

        setPadding(
            8.dp(context).toInt(),
            8.dp(context).toInt(),
            8.dp(context).toInt(),
            8.dp(context).toInt()
        )
        
    }

    private fun bind() {
        company?.let { company ->
            binding.logoImageView.load(
                context = context,
                imageUrl = company.logo,
                placeholderResourceId = R.drawable.image_placeholder_circle
            )
            binding.logoImageView.isVisible = company.logo.isNullOrEmpty().not()
            binding.middleSpace.isVisible = company.logo.isNullOrEmpty().not()
            binding.companyNameTextView.text = company.name
        }
        setOnClickListener {
            company?.let {
                clickListener?.onItemClick(it)
            }
        }
    }
}