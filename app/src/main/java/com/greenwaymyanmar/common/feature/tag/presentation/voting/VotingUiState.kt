package com.greenwaymyanmar.common.feature.tag.presentation.voting

import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag

data class VotingUiState(
    val category: String? = "",
    val crop: String? = "",
    val votableTags: List<UiVotableTag> = emptyList(),
    val associatedVotableTags: List<UiVotableTag> = emptyList()
)