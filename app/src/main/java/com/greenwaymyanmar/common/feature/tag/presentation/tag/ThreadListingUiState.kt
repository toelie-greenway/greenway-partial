package com.greenwaymyanmar.common.feature.tag.presentation.tag

import androidx.paging.PagedList
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Thread

data class ThreadListingUiState(
    val list: PagedList<Thread>? = null,
    val networkState: NetworkState? = null,
    val refreshState: NetworkState? = null,
    val hasMore: Boolean = false
) {
    companion object {
        val Empty = ThreadListingUiState()
    }
}