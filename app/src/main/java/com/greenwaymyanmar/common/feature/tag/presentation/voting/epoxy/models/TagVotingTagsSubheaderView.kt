package com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
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

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    @ModelProp
    fun setCategory(category: UiCategory?) {
        binding.categoryChip.text =
            category?.name ?: resources.getString(R.string.tag_voting_action_change_category)
    }
}