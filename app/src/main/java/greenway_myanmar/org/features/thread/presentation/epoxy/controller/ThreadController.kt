package greenway_myanmar.org.features.thread.presentation.epoxy.controller

import android.view.View.OnClickListener
import com.airbnb.epoxy.TypedEpoxyController
import greenway_myanmar.org.features.thread.presentation.ThreadUiState
import greenway_myanmar.org.features.thread.presentation.epoxy.models.ThreadTagVoteOptionsViewModel_

class ThreadController constructor(
    private val onGoToVoteClicked: () -> Unit
) : TypedEpoxyController<ThreadUiState>() {

    override fun buildModels(data: ThreadUiState?) {
        data?.let {
            buildTagVoteUi(it)
//            val votableTags = it.votedTags
//            group {
//                id("voted_tags")
//                layout(R.layout.thread_voted_tags_view)
//
//                votableTags.forEachIndexed { index, votableTag ->
//                    if (index > 0) {
//                        threadTagVotedListDividerView {
//                            id("voted-tag-divider-${votableTag.tag.id}")
//                        }
//                    }
//                    threadTagVotedListItemView {
//                        id("voted-tag-${votableTag.tag.id}")
//                        tag(votableTag)
//                    }
//                }
//
//                if (it.isUserVotable) {
//                    threadTagVotedListDividerView {
//                        id("vote-tag-button-divider")
//                    }
//                    threadTagVoteButtonView {
//                        id("vote-tag-button")
//                        clickCallback(OnClickListener {
//                            onGoToVoteClicked()
//                        })
//                    }
//                }
//            }
        }
    }

    private fun buildTagVoteUi(threadUiState: ThreadUiState) {
        if (shouldHideVoteUi(threadUiState)) {
            return
        }

        ThreadTagVoteOptionsViewModel_()
            .id("tag-vote-options-view")
            .tags(threadUiState.tagVoteOptions)
            .technician(threadUiState.isTechnician)
            .hasApproved(threadUiState.hasApprovedTag)
            .voteClickListener(
                OnClickListener {
                    onGoToVoteClicked()
                }
            )
            .addTo(this)
    }

    private fun shouldHideVoteUi(threadUiState: ThreadUiState) =
        (!threadUiState.isTechnician && threadUiState.tagVoteOptions.isEmpty()) ||
                (threadUiState.tagVoteOptions.isEmpty() && threadUiState.hasApprovedTag)
}