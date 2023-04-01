package com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.controller

import android.view.View
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyModel
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagPost
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagThread
import com.greenwaymyanmar.common.feature.tag.presentation.tag.TagListingUiState
import com.greenwaymyanmar.common.feature.tag.presentation.tag.TagUiState
import com.greenwaymyanmar.common.feature.tag.presentation.tag.UiTagTab
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagHeaderView
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagHeaderViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagMoreThreadPostViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagPostItemViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagThreadItemViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.tagThreadNetworkStateItemView
import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Post
import greenway_myanmar.org.vo.Product
import greenway_myanmar.org.vo.Thread
import kotlinx.datetime.Clock
import kotlinx.datetime.toKotlinInstant
import timber.log.Timber
import kotlin.reflect.KClass

class TagController(
    private val onTabChanged: (tab: UiTagTab) -> Unit,
    private val onMoreThreadClicked: () -> Unit,
    private val onMorePostClicked: () -> Unit,
    private val onMoreProductClicked: () -> Unit
) : Typed2PagedListEpoxyController<Thread, TagThreadItemViewModel_, Post, TagPostItemViewModel_>() {

    var uiState: TagUiState? = null
        set(value) {
            Timber.d("uiState: $uiState")
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

    private var threadListingUiState: TagListingUiState = TagListingUiState.Empty
        set(value) {
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

    private var postListingUiState: TagListingUiState = TagListingUiState.Empty
        set(value) {
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

    private var productListingUiState: TagListingUiState = TagListingUiState.Empty
        set(value) {
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

    override fun buildFirstItemModel(currentPosition: Int, item: Thread?): TagThreadItemViewModel_ {
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

    override fun buildSecondItemModel(
        currentPosition: Int,
        item: Post?
    ): TagPostItemViewModel_ {
        if (item == null) return TagPostItemViewModel_()
            .id("product-null-item")

        Timber.d("Post: $item")
        return TagPostItemViewModel_()
            .id("post-${item.id}")
            .tagPost(
                UiTagPost(
                    id = item.id,
                    title = item.title,
                    author = item.author?.name.orEmpty(),
                    createdAt = item.createdAt.toInstant().toKotlinInstant(),
                    imageUrl = item.coverImageUrl
                )
            )
    }

    override fun addModels(firstModels: List<EpoxyModel<*>>, secondModels: List<EpoxyModel<*>>) {
        uiState?.let {
            buildHeaderUi(it.tag, it.tab)
            buildPageContents(it, firstModels, secondModels)
          //  buildProductsUi(secondModels)
        }
    }

    private fun buildHeaderUi(loadingState: LoadingState<UiTag>, tab: UiTagTab) {
        when (loadingState) {
            is LoadingState.Success -> {
                TagHeaderViewModel_()
                    .id("header")
                    .tag(loadingState.data)
                    .selectedTab(tab)
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

    private fun buildPageContents(
        tagUiState: TagUiState,
        firstModels: List<EpoxyModel<*>>,
        secondModels: List<EpoxyModel<*>>
    ) {
        if (tagUiState.tab == UiTagTab.Thread) {
            buildThreadsUi(firstModels)
            buildMoreThreadButtonUi()
        } else if (tagUiState.tab == UiTagTab.Post) {
            buildPostsUi(secondModels)
            buildMorePostButtonUi()
        }
    }

    private fun buildThreadsUi(models: List<EpoxyModel<*>>) {
        super.addFirstModels(models)
        if (hasThreadExtraRow()) {
            tagThreadNetworkStateItemView {
                id("thread-network-state")
                networkState(threadListingUiState.networkState)
                retryCallback(View.OnClickListener {

                })
            }
        }
    }

    private fun buildPostsUi(models: List<EpoxyModel<*>>) {
        super.addSecondModels(models)
        if (hasPostExtraRow()) {
            tagThreadNetworkStateItemView {
                id("post-network-state")
                networkState(postListingUiState.networkState)
                retryCallback(View.OnClickListener {

                })
            }
        }
    }

    private fun buildProductsUi(models: List<EpoxyModel<*>>) {
//        TagProductSubheaderViewModel_()
//            .id("product-subheader")
//            .addTo(this)
//
//        super.addSecondModels(models)
//        if (hasProductExtraRow()) {
//            //TODO: create product item placeholder
//            tagThreadNetworkStateItemView {
//                id("product-network-state")
//                networkState(productListingUiState.networkState)
//                retryCallback(View.OnClickListener {
//
//                })
//            }
//        }
//        buildMoreProductButtonUi()
    }

    private fun buildMoreThreadButtonUi() {
        if (threadListingUiState.networkState == NetworkState.LOADED &&
            threadListingUiState.hasMore
        ) {
            TagMoreThreadPostViewModel_()
                .id("more-thread")
                .onMoreClickCallback(View.OnClickListener {
                    onMoreThreadClicked()
                })
                .addTo(this)
        }
    }

    private fun buildMorePostButtonUi() {
        if (postListingUiState.networkState == NetworkState.LOADED &&
            postListingUiState.hasMore
        ) {
            TagMoreThreadPostViewModel_()
                .id("more-post")
                .onMoreClickCallback(View.OnClickListener {
                    onMorePostClicked()
                })
                .addTo(this)
        }
    }

    private fun buildMoreProductButtonUi() {
        if (productListingUiState.networkState == NetworkState.LOADED &&
            productListingUiState.hasMore
        ) {
            TagMoreThreadPostViewModel_()
                .id("more-product")
                .onMoreClickCallback(View.OnClickListener {
                    onMoreProductClicked()
                })
                .addTo(this)
        }
    }


    override fun getFirstModelClass(): KClass<TagThreadItemViewModel_> {
        return TagThreadItemViewModel_::class
    }

    override fun getSecondModelClass(): KClass<TagPostItemViewModel_> {
        return TagPostItemViewModel_::class
    }

    fun setThreadNetworkState(networkState: NetworkState?) {
        threadListingUiState = threadListingUiState.copy(
            networkState = networkState
        )
    }

    fun setHasMoreThread(hasMore: Boolean?) {
        Timber.d("setThreadHasMore: $hasMore")
        threadListingUiState = threadListingUiState.copy(
            hasMore = hasMore == true
        )
    }

    private fun hasThreadExtraRow(): Boolean {
        return threadListingUiState.networkState != NetworkState.LOADED
    }

    fun setThreadPagedList(list: PagedList<Thread>) {
        submitFirstList(list)
    }

    fun setPostPagedList(list: PagedList<Post>) {
        submitSecondList(list)
    }

    fun setPostNetworkState(networkState: NetworkState?) {
        postListingUiState = postListingUiState.copy(
            networkState = networkState
        )
    }

    fun setHasMorePost(hasMore: Boolean?) {
        Timber.d("setHasMorePost: $hasMore")
        postListingUiState = postListingUiState.copy(
            hasMore = hasMore == true
        )
    }

    private fun hasPostExtraRow(): Boolean {
        return postListingUiState.networkState != NetworkState.LOADED
    }

    fun setProductPagedList(list: PagedList<Product>) {
        // TODO: submitSecondList(list)
    }

    fun setProductNetworkState(networkState: NetworkState?) {
        productListingUiState = productListingUiState.copy(
            networkState = networkState
        )
    }

    fun setHasMoreProduct(hasMore: Boolean?) {
        Timber.d("setHasMoreProduct: $hasMore")
        productListingUiState = productListingUiState.copy(
            hasMore = hasMore == true
        )
    }

    private fun hasProductExtraRow(): Boolean {
        return productListingUiState.networkState != NetworkState.LOADED
    }

}