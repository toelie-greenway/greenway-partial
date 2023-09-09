package com.greenwaymyanmar.common.feature.tag.presentation.voting

import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag

sealed class VotingEvent {
    data class OnVoteChanged(val votableTag: UiVotableTag) : VotingEvent()
    data class OnQueryChanged(val query: String?) : VotingEvent()
    data class OnCustomCategoryChanged(val category: UiCategory?) : VotingEvent()

    object OnSubmit : VotingEvent()
}