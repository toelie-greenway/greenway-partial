package greenway_myanmar.org.features.thread.presentation.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.databinding.ThreadTagVotedListItemViewBinding
import greenway_myanmar.org.ui.widget.CheckableLinearLayout
import greenway_myanmar.org.util.extensions.dp

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ThreadTagVotedListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CheckableLinearLayout(context, attrs) {

    private val binding = ThreadTagVotedListItemViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        minimumHeight = 56.dp(context).toInt()
        gravity = Gravity.CENTER_VERTICAL
        setPadding(16.dp(context).toInt(), 0, 16.dp(context).toInt(), 0)
    }

    @ModelProp
    fun setTag(tag: UiVotableTag) {
        isChecked = tag.isVoted
        binding.tagName.text = tag.tag.name
        binding.voteCountTextView.text = numberFormat.format(tag.voteCount)
        binding.voteCountTextView.isVisible = tag.voteCount > 0
    }
}