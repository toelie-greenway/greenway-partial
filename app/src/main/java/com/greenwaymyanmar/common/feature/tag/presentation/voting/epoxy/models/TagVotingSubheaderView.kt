package com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import greenway_myanmar.org.databinding.TagVotingSubheaderViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagVotingSubheaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val binding = TagVotingSubheaderViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    @TextProp
    fun setSubheader(text: CharSequence) {
        binding.subheaderTextView.text = text
    }
}