package greenway_myanmar.org.features.tag.presentation.epoxy.controller

import com.airbnb.epoxy.TypedEpoxyController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.tag.presentation.TagUiState
import greenway_myanmar.org.features.tag.presentation.UiTagTab
import greenway_myanmar.org.features.tag.presentation.epoxy.models.TagHeaderView
import greenway_myanmar.org.features.tag.presentation.epoxy.models.TagHeaderViewModel_
import greenway_myanmar.org.features.tag.presentation.epoxy.models.TagMoreThreadPostViewModel_
import greenway_myanmar.org.features.tag.presentation.epoxy.models.TagPostItemViewModel_
import greenway_myanmar.org.features.tag.presentation.epoxy.models.TagProductItemViewModel_
import greenway_myanmar.org.features.tag.presentation.epoxy.models.TagProductSubheaderViewModel_
import greenway_myanmar.org.features.tag.presentation.epoxy.models.TagThreadItemViewModel_
import greenway_myanmar.org.features.tag.presentation.model.UiTag
import greenway_myanmar.org.features.tag.presentation.model.UiTagPost
import greenway_myanmar.org.features.tag.presentation.model.UiTagProduct
import greenway_myanmar.org.features.tag.presentation.model.UiTagThread

class TagController(
    private val onTabChanged: (tab: UiTagTab) -> Unit
) : TypedEpoxyController<TagUiState>() {

    override fun buildModels(data: TagUiState?) {
        data?.let {
            buildHeaderUi(it.tag)

            if (it.tab == UiTagTab.Thread) {
                buildThreadsUi(it.threads)
            } else if (it.tab == UiTagTab.Post) {
                buildPostsUi(it.posts)
            }

            buildMoreButtonUi()
            buildProductsUi(it.products)
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

    private fun buildThreadsUi(loadingState: LoadingState<List<UiTagThread>>) {
        when (loadingState) {
            is LoadingState.Success -> {
                loadingState.data.forEach {
                    TagThreadItemViewModel_()
                        .id("thread-${it.id}")
                        .tagThread(it)
                        .addTo(this)
                }
            }
            else -> {

            }
        }
    }

    private fun buildMoreButtonUi() {
        TagMoreThreadPostViewModel_()
            .id("more")
            .addTo(this)
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
}