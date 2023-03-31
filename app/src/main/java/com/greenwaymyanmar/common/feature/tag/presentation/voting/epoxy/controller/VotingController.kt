package com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.controller

import android.view.View.OnClickListener
import com.airbnb.epoxy.TypedEpoxyController
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.common.feature.tag.presentation.voting.VotingUiState
import com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models.tagVotingSubheaderView
import com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models.tagVotingVotableTagItemView
import greenway_myanmar.org.R

class VotingController constructor(
    private val onVoteClicked: (UiVotableTag) -> Unit
): TypedEpoxyController<VotingUiState>() {

    override fun buildModels(data: VotingUiState?) {
        data?.let {
            tagVotingSubheaderView {
                id("votable-tags-subheader")
                subheader(R.string.thread_tag_voting_voted_tags_subheader)
            }
            it.votableTags.forEach { votableTag ->
                tagVotingVotableTagItemView {
                    id("votable-tag-${votableTag.tag.id}")
                    votableTag(votableTag)
                    onVoteClickListener(OnClickListener {
                        onVoteClicked(votableTag)
                    })
                }
            }
        }
    }
}