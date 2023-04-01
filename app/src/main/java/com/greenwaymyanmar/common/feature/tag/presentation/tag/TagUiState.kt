package com.greenwaymyanmar.common.feature.tag.presentation.tag

import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.core.presentation.model.LoadingState

data class TagUiState(
    val tag: LoadingState<UiTag> = LoadingState.Idle,
    val tab: UiTagTab = UiTagTab.Thread,
) {
    companion object {
        val Empty = TagUiState()
    }
}

enum class UiTagTab(val position: Int) {
    Thread(0),
    Post(1);

    companion object {
        fun fromPosition(position: Int): UiTagTab {
            return when (position) {
                Thread.position -> Thread
                Post.position -> Post
                else -> throw IllegalArgumentException("Invalid position $position")
            }
        }
    }
}