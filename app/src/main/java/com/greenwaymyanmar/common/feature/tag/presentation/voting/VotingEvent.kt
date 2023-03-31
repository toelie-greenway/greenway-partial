package com.greenwaymyanmar.common.feature.tag.presentation.voting

import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag

sealed class VotingEvent {
    data class OnToggleVote(val votableTag: UiVotableTag): VotingEvent()
}