package com.greenwaymyanmar.common.feature.tag.presentation.tag

import greenway_myanmar.org.vo.NetworkState

data class ThreadListingUiState(
    val networkState: NetworkState? = null,
    val hasMore: Boolean = false
) {
    companion object {
        val Empty = ThreadListingUiState()
    }
}