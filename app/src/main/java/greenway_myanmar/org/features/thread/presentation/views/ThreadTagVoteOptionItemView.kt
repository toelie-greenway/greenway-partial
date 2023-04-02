package greenway_myanmar.org.features.thread.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.view.isVisible
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.databinding.ThreadTagVoteOptionItemViewBinding
import greenway_myanmar.org.ui.widget.CheckableLinearLayout
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.kotlin.customViewMergeBinding

class ThreadTagVoteOptionItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CheckableLinearLayout(context, attrs) {

    private val binding = customViewMergeBinding(ThreadTagVoteOptionItemViewBinding::inflate)

    init {
        minimumHeight = 56.dp(context).toInt()
        gravity = Gravity.CENTER_VERTICAL
        setPadding(16.dp(context).toInt(), 0, 16.dp(context).toInt(), 0)
    }

    fun bind(tag: UiVotableTag) {
        isChecked = tag.isVoted
        binding.tagName.text = tag.tag.name
        binding.voteCountTextView.text = numberFormat.format(tag.voteCount)
        binding.voteCountTextView.isVisible = tag.voteCount > 0
    }
}