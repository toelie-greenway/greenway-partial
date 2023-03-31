package com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import greenway_myanmar.org.databinding.TagVotingVotableTagItemViewBinding
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.extensions.load

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT, fullSpan = false)
class TagVotingVotableTagItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding = TagVotingVotableTagItemViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    var onVoteClickListener: OnClickListener? = null
        @CallbackProp set

    init {
        orientation = VERTICAL
    }

    @ModelProp
    fun setVotableTag(votableTag: UiVotableTag) {
        binding.tagNameTextView.text = votableTag.tag.name
        binding.voteCountTextView.text = votableTag.voteCount.toString()
        binding.imageView.load(context, votableTag.tag.imageUrls.firstOrNull())
        binding.userVoteIndicatorTextView.isVisible = votableTag.isVoted
        binding.cardView.isChecked = votableTag.isVoted
        if (votableTag.isVoted) {
            binding.cardView.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#007AFF")))
            binding.cardView.strokeWidth = 2.dp(context).toInt()
        } else {
            binding.cardView.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#E0E0E0")))
            binding.cardView.strokeWidth = 1.dp(context).toInt()
        }
        binding.cardView.setOnClickListener {
            onVoteClickListener?.onClick(it)
        }

    }
}
