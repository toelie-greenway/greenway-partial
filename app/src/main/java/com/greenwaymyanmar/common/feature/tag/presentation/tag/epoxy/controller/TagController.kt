package com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.controller

import android.view.View
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.group
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagPost
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagProduct
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagThread
import com.greenwaymyanmar.common.feature.tag.presentation.tag.TagUiState
import com.greenwaymyanmar.common.feature.tag.presentation.tag.ThreadListingUiState
import com.greenwaymyanmar.common.feature.tag.presentation.tag.UiTagTab
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagHeaderView
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagHeaderViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagMoreThreadPostViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagPostItemViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagProductItemViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagProductSubheaderViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagThreadItemViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.tagThreadItemView
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.tagThreadNetworkStateItemView
import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.R
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Thread
import kotlinx.datetime.Clock
import timber.log.Timber

class TagController(
    private val onTabChanged: (tab: UiTagTab) -> Unit,
    private val onMoreThreadClicked: () -> Unit,
    private val onMorePostClicked: () -> Unit
) : PagedListEpoxyController<Thread>() {

    var uiState: TagUiState? = null
        set(value) {
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

    var threadListingUiState: ThreadListingUiState = ThreadListingUiState.Empty
        set(value) {
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

    override fun buildItemModel(currentPosition: Int, item: Thread?): EpoxyModel<*> {
        if (item == null) return TagThreadItemViewModel_()
            .id("thread-null-item")

        Timber.d("Thread: $item")

        return TagThreadItemViewModel_()
            .id("thread-${item.id}")
            .tagThread(
                UiTagThread(
                    id = item.id,
                    question = item.body.orEmpty(),
                    createdAt = Clock.System.now(),
                    imageUrl = null
                )
            )
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        uiState?.let {
            buildHeaderUi(it.tag)
            buildPageContents(it, models)
            buildMoreButtonUi()
            buildProductsUi(it.products)
        }
    }

    private fun buildPageContents(
        tagUiState: TagUiState,
        models: List<EpoxyModel<*>>
    ) {
        if (tagUiState.tab == UiTagTab.Thread) {
            // buildThreadsUi()
            super.addModels(models)
            if (hasThreadExtraRow()) {
                tagThreadNetworkStateItemView {
                    id("thread-network-state")
                    networkState(threadListingUiState.networkState)
                    retryCallback(View.OnClickListener {

                    })
                }
            }
        } else if (tagUiState.tab == UiTagTab.Post) {
            buildPostsUi(tagUiState.posts)
        }
    }

    private fun buildHeaderUi(loadingState: LoadingState<UiTag>) {
        when (loadingState) {
            is LoadingState.Success -> {
                TagHeaderViewModel_()
                    .id("header")
                    .tag(loadingState.data)
                    .clickCallback(object : TagHeaderView.TagHeaderClickCallback {
                        override fun onTabChanged(tab: UiTagTab) {
                            this@TagController.onTabChanged(tab)
                        }
                    })
                    .addTo(this)
            }
            else -> {

            }
        }
    }

    private fun buildPostsUi(loadingState: LoadingState<List<UiTagPost>>) {
        when (loadingState) {
            is LoadingState.Success -> {
                loadingState.data.forEach {
                    TagPostItemViewModel_()
                        .id("post-${it.id}")
                        .tagPost(it)
                        .addTo(this)
                }
            }
            else -> {

            }
        }
    }

    private fun buildThreadsUi() {
        Timber.d("buildThreadsUi: ${threadListingUiState.list?.size}")
        if (threadListingUiState.list.isNullOrEmpty()) return

        group {
            id("content-list-group")
            layout(R.layout.tag_thread_post_list_group_view)

            threadListingUiState.list?.forEach { thread ->
                Timber.d("Thread: $thread")
                if (thread != null) {

                    tagThreadItemView {
                        id("thread-${thread.id}")
                        tagThread(
                            UiTagThread(
                                id = thread.id,
                                question = thread.body.orEmpty(),
                                createdAt = Clock.System.now(),
                                imageUrl = null
                            )
                        )
                    }
                }
            }
        }
        Timber.d("buildThreadsUi End!")
    }

    private fun buildMoreButtonUi() {
        if (threadListingUiState.networkState == NetworkState.LOADED &&
            threadListingUiState.hasMore
        ) {
            TagMoreThreadPostViewModel_()
                .id("more")
                .onMoreClickCallback(View.OnClickListener {
                    if (uiState?.tab == UiTagTab.Thread) {
                        onMoreThreadClicked()
                    } else if (uiState?.tab == UiTagTab.Post) {
                        onMorePostClicked()
                    }
                })
                .addTo(this)
        }
    }

    private fun buildProductsUi(products: LoadingState<List<UiTagProduct>>) {
        when (products) {
            is LoadingState.Success -> {
                TagProductSubheaderViewModel_()
                    .id("product-subheader")
                    .addTo(this)

                products.data.forEach {
                    TagProductItemViewModel_()
                        .id("product-${it.id}")
                        .tagProduct(it)
                        .addTo(this)
                }
            }
            else -> {

            }
        }
    }

    fun setThreadPagedList(list: PagedList<Thread>) {
        threadListingUiState = threadListingUiState.copy(
            list = list
        )
    }

    fun setThreadNetworkState(networkState: NetworkState?) {
        threadListingUiState = threadListingUiState.copy(
            networkState = networkState
        )
    }

    fun setThreadHasMore(hasMore: Boolean?) {
        Timber.d("setThreadHasMore: $hasMore")
        threadListingUiState = threadListingUiState.copy(
            hasMore = hasMore == true
        )
    }

    private fun hasThreadExtraRow(): Boolean {
        return threadListingUiState.networkState != NetworkState.LOADED
    }
}