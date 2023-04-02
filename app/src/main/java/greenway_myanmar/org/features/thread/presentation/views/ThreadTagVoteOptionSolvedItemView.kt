package greenway_myanmar.org.features.thread.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.databinding.ThreadTagVoteOptionSolvedItemViewBinding
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.kotlin.customViewMergeBinding

class ThreadTagVoteOptionSolvedItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding = customViewMergeBinding(ThreadTagVoteOptionSolvedItemViewBinding::inflate)

    init {
        minimumHeight = 56.dp(context).toInt()
        gravity = Gravity.CENTER_VERTICAL
    }

    fun bind(tag: UiVotableTag) {
        binding.tagName.text = tag.tag.name
        binding.voteCountTextView.text = numberFormat.format(tag.voteCount)
        binding.voteCountTextView.isVisible = tag.voteCount > 0
    }
}