package com.greenwaymyanmar.common.feature.tag.presentation.voting

import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.core.presentation.model.LoadingState

data class VotingUiState(
    val category: UiCategory? = null,
    val cropOrAnimalName: String? = "",
    val voteOptions: List<UiVotableTag> = emptyList(),
    val searchQuery: String = "",
    val searchResults: LoadingState<List<UiVotableTag>>? = null,
    val votedTag: UiVotableTag? = null,
    val customCategory: UiCategory? = null
) {
    val showVoteOptions: Boolean = searchQuery.isEmpty() && voteOptions.isNotEmpty()
}