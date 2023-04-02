package greenway_myanmar.org.features.thread.presentation.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.ThreadTagVoteOptionsViewBinding
import greenway_myanmar.org.features.thread.presentation.views.ThreadTagVoteOptionItemView
import greenway_myanmar.org.features.thread.presentation.views.ThreadTagVoteOptionSolvedItemView
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.kotlin.customViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ThreadTagVoteOptionsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(ThreadTagVoteOptionsViewBinding::inflate)

    var technician: Boolean = false
        @ModelProp set

    var hasApproved: Boolean = false
        @ModelProp set

    @ModelProp
    lateinit var tags: List<UiVotableTag>

    var voteClickListener: OnClickListener? = null
        @CallbackProp set

    private var _expand: Boolean = false

    private val divider: View by lazy {
        View(context).apply {
            layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.dp(context).toInt()
                )
            setBackgroundColor(ContextCompat.getColor(context, R.color.app_divider))
        }
    }

    init {
        binding.goToVoteButton.setOnClickListener {
            voteClickListener?.onClick(it)
        }
    }

    @AfterPropsSet
    fun bind() {
        binding.approvedStateContainer.isVisible = hasApproved
        binding.unapprovedStateContainer.isVisible = !hasApproved
        if (hasApproved) {
            bindExpandToggleButton()
        } else {
            bindTitle(technician)
            bindVoteButton(technician)
        }
        bindTagVoteOptions(tags)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateApprovedTagVoteOptionsVisibility()
    }

    private fun bindTitle(technician: Boolean) {
        binding.titleTextView.setText(
            if (technician) {
                R.string.thread_tag_vote_technician_subheader
            } else {
                R.string.thread_tag_vote_non_technician_subheader
            }
        )
    }

    private fun bindExpandToggleButton() {
        binding.expandToggleApprovedOptionsButton.setOnClickListener {
            _expand = !_expand
            updateApprovedTagVoteOptionsVisibility()
        }
    }

    private fun updateApprovedTagVoteOptionsVisibility() {
        binding.approvedTagVoteOptionsContainer.isVisible = _expand
        binding.expandToggleApprovedOptionsButton.setText(
            if (_expand) {
                R.string.thread_tag_vote_solved_action_collapse
            } else {
                R.string.thread_tag_vote_solved_action_expand
            }
        )
        binding.expandToggleApprovedOptionsButton.setIconResource(
            if (_expand) {
                R.drawable.ic_expand_less_black_24dp
            } else {
                R.drawable.ic_expand_more_black_24dp
            }
        )
    }

    private fun bindVoteButton(technician: Boolean) {
        binding.goToVoteButton.isVisible = technician
    }

    private fun bindTagVoteOptions(tags: List<UiVotableTag>) {
        val container = if (hasApproved) {
            binding.approvedTagVoteOptionsContainer
        } else {
            binding.unapprovedTagVoteOptionsContainer
        }
        binding.emptyTextView.isVisible = tags.isEmpty()
        container.isVisible = tags.isNotEmpty()
        container.removeAllViews()
        tags.forEach { tag ->
            container.addView(
                if (hasApproved) {
                    createTagVoteOptionSolvedItemView(tag)
                } else {
                    createTagVoteOptionItemView(tag)
                }
            )
            if (shouldDrawDivider()) {
                container.addView(divider)
            }
        }
    }

    private fun createTagVoteOptionItemView(tag: UiVotableTag) =
        ThreadTagVoteOptionItemView(context).apply {
            bind(
                tag = tag.copy(
                    isVoted = if (technician) {
                        tag.isVoted
                    } else {
                        true
                    }
                ),
            )
        }

    private fun createTagVoteOptionSolvedItemView(tag: UiVotableTag) =
        ThreadTagVoteOptionSolvedItemView(context).apply {
            bind(
                tag = tag.copy(
                    isVoted = if (technician) {
                        tag.isVoted
                    } else {
                        true
                    }
                ),
            )
        }

    private fun shouldDrawDivider() = technician && !hasApproved

}