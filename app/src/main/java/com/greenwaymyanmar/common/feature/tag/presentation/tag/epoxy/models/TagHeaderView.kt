package com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.tag.UiTagTab
import greenway_myanmar.org.databinding.TagHeaderViewBinding
import greenway_myanmar.org.util.extensions.load

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = TagHeaderViewBinding.inflate(LayoutInflater.from(context), this, true)
    var clickCallback: TagHeaderClickCallback? = null
        @CallbackProp set

    init {
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position != null) {
                    clickCallback?.onTabChanged(UiTagTab.fromPosition(position))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    @ModelProp
    fun setTag(tag: UiTag) {
        binding.heroImageView.load(context, tag.imageUrls.firstOrNull())
        binding.tagNameTextView.text = tag.name
        binding.categoryNameTextView.text = tag.category.name
    }

    interface TagHeaderClickCallback {
        fun onTabChanged(tab: UiTagTab)
    }
}
