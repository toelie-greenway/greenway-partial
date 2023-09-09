package com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.TagVotingTagsSubheaderViewBinding
import greenway_myanmar.org.util.kotlin.customViewMergeBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagVotingTagsSubheaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    val binding = customViewMergeBinding(TagVotingTagsSubheaderViewBinding::inflate)

    var categoryClickCallback: OnClickListener? = null
        @CallbackProp set

    var clearCategoryClickCallback: OnClickListener? = null
        @CallbackProp set

    var category: UiCategory? = null
        @ModelProp set(value) {
            if (field != null && field == value) {
                return
            }

            field = value
            bind(value)
        }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        binding.categoryChip.setOnClickListener {
            categoryClickCallback?.onClick(it)
        }
        binding.categoryChip.setOnCloseIconClickListener {
            clearCategoryClickCallback?.onClick(it)
        }
    }

    private fun bind(category: UiCategory?) {
        binding.categoryChip.text =
            category?.name ?: resources.getString(R.string.tag_voting_action_change_category)
        binding.categoryChip.isCloseIconVisible = category != null
        binding.categoryChip.isChipIconVisible = category == null
    }
}