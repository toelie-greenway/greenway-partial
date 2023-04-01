package com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagProduct
import greenway_myanmar.org.databinding.TagProductItemViewBinding
import greenway_myanmar.org.util.extensions.load

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagProductItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding =
        TagProductItemViewBinding.inflate(LayoutInflater.from(context), this, true)

    @ModelProp
    fun setTagProduct(product: UiTagProduct) {
        binding.productNameTextView.text = product.productName
        binding.distributorNameTextView.text = product.distributorName
        binding.tagColorIndicatorsView.setTags(
            listOf(
                Color.BLUE,
                Color.RED
            )
        )
        bindImage(product.imageUrl)
    }

    private fun bindImage(imageUrl: String?) {
        binding.imageView.load(context, imageUrl.orNull())
        binding.imageView.isVisible = true
        binding.imageMarginEndSpace.isVisible = true
    }

    private fun String?.orNull() = if (this.isNullOrEmpty()) {
        null
    } else {
        this
    }
}