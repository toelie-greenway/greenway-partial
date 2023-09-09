package com.greenwaymyanmar.common.feature.tag.presentation.voting

import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text

data class VotingUiState(
    val category: UiCategory? = null,
    val cropOrAnimalName: String? = "",
    val voteOptions: List<UiVotableTag> = emptyList(),
    val searchQuery: String = "",
    val searchResults: LoadingState<List<UiVotableTag>>? = null,
    val currentVotedTag: UiVotableTag? = null,
    val previousVotedTag: UiVotableTag? = null,
    val customCategory: UiCategory? = null,

    val tagError: Text? = null,
    val submittingLoadingState: LoadingState<Boolean> = LoadingState.Idle
) {
    val showVoteOptions: Boolean = searchQuery.isEmpty() && voteOptions.isNotEmpty()
    val submitButtonLabel: Int = if (previousVotedTag == null) {
        R.string.tag_action_vote
    } else {
        if (currentVotedTag == null) {
            R.string.tag_action_remove_vote
        } else {
            R.string.tag_action_update_vote
        }
    }
}