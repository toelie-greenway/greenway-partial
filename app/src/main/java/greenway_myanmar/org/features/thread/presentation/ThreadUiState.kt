package greenway_myanmar.org.features.thread.presentation

import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag


data class ThreadUiState(
    val tagVoteOptions: List<UiVotableTag> = emptyList(),
    val approvedTags: List<UiTag> = emptyList(),
    val isTechnician: Boolean = false,
) {
    val hasApprovedTag: Boolean = approvedTags.isNotEmpty()
}