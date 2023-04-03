package com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.TagVotingOptionsSubheaderViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagVotingOptionsSubheaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val binding = TagVotingOptionsSubheaderViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )
}