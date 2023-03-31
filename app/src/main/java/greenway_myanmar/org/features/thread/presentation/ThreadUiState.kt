package greenway_myanmar.org.features.thread.presentation

import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag


data class ThreadUiState(
    val votedTags: List<UiVotableTag> = emptyList(),
    val isUserVotable: Boolean = true
)