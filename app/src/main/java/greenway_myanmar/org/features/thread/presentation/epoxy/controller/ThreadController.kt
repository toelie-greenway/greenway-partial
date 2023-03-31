package greenway_myanmar.org.features.thread.presentation.epoxy.controller

import android.view.View.OnClickListener
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.epoxy.group
import greenway_myanmar.org.R
import greenway_myanmar.org.features.thread.presentation.ThreadUiState
import greenway_myanmar.org.features.thread.presentation.epoxy.models.threadTagVoteButtonView
import greenway_myanmar.org.features.thread.presentation.epoxy.models.threadTagVotedListDividerView
import greenway_myanmar.org.features.thread.presentation.epoxy.models.threadTagVotedListItemView

class ThreadController constructor(
    private val onGoToVoteClicked: () -> Unit
) : TypedEpoxyController<ThreadUiState>() {
    override fun buildModels(data: ThreadUiState?) {
        data?.let {
            val votableTags = it.votedTags
            group {
                id("voted_tags")
                layout(R.layout.thread_voted_tags_view)

                votableTags.forEachIndexed { index, votableTag ->
                    if (index > 0) {
                        threadTagVotedListDividerView {
                            id("voted-tag-divider-${votableTag.tag.id}")
                        }
                    }
                    threadTagVotedListItemView {
                        id("voted-tag-${votableTag.tag.id}")
                        tag(votableTag)
                    }
                }

                if (it.isUserVotable) {
                    threadTagVotedListDividerView {
                        id("vote-tag-button-divider")
                    }
                    threadTagVoteButtonView {
                        id("vote-tag-button")
                        clickCallback(OnClickListener {
                            onGoToVoteClicked()
                        })
                    }
                }
            }
        }
    }
}