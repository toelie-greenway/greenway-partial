package greenway_myanmar.org.features.thread.presentation.epoxy.controller

import android.view.View.OnClickListener
import androidx.paging.PagedList
import com.airbnb.epoxy.group
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTagProduct
import com.greenwaymyanmar.common.feature.tag.presentation.tag.TagListingUiState
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagMoreThreadPostViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagProductItemViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models.TagProductSubheaderViewModel_
import com.greenwaymyanmar.common.ui.epoxy.controller.TypedPagedListEpoxyController
import com.greenwaymyanmar.common.ui.epoxy.model.listNetworkStateItemView
import greenway_myanmar.org.R
import greenway_myanmar.org.features.thread.presentation.ThreadUiState
import greenway_myanmar.org.features.thread.presentation.epoxy.models.ThreadTagVoteOptionsViewModel_
import greenway_myanmar.org.features.thread.presentation.models.threadTagItemView
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Product
import timber.log.Timber

class ThreadController constructor(
    private val onGoToVoteClicked: () -> Unit,
    private val onMoreProductClicked: () -> Unit
) : TypedPagedListEpoxyController<Product, TagProductItemViewModel_>() {

    var threadUiState: ThreadUiState? = null
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

    override fun buildItemModel(currentPosition: Int, item: Product?): TagProductItemViewModel_ {
        if (item == null) return TagProductItemViewModel_()
            .id("product-null-item")

        Timber.d("Product: $item")

        return TagProductItemViewModel_()
            .id("product-${item.id}")
            .tagProduct(
                UiTagProduct(
                    id = item.id,
                    productName = item.name.orEmpty(),
                    distributorName = item.distributors?.firstOrNull()?.name.orEmpty(),
                    imageUrl = item.thumbnail
                )
            )
    }

    override fun addModels(models: List<TagProductItemViewModel_>) {
        threadUiState?.let {
            buildTagVoteUi(it)
            buildApprovedTags(it.category, it.approvedTags)
            buildProductsUi(models)
        }
    }

    private fun buildTagVoteUi(threadUiState: ThreadUiState) {
        if (shouldHideVoteUi(threadUiState)) {
            return
        }

        ThreadTagVoteOptionsViewModel_()
            .id("tag-vote-options-view")
            .tags(threadUiState.tagVoteOptions)
            .technician(threadUiState.isTechnician)
            .hasApproved(threadUiState.hasApprovedTag)
            .voteClickListener(
                OnClickListener {
                    onGoToVoteClicked()
                }
            )
            .addTo(this)
    }

    private fun buildApprovedTags(threadCategory: UiCategory?, tags: List<UiTag>) {
        if (tags.isEmpty() || threadCategory == null) return

        group {
            id("approved-tags")
            layout(R.layout.thread_tags_group_view)

            tags.forEachIndexed { index, tag ->
                threadTagItemView {
                    id("approved-tag-${tag.id}")
                    tag(
                        tag.copy(
                            category = tag.category ?: threadCategory
                        )
                    )
                    index(index)
                }
            }
        }
    }

    private fun buildProductsUi(models: List<TagProductItemViewModel_>) {
        TagProductSubheaderViewModel_()
            .id("product-subheader")
            .addTo(this)

        super.addModels(models)
        if (hasProductExtraRow()) {
            listNetworkStateItemView {
                id("product-network-state")
                loadingView(R.layout.tag_product_list_item_loading_view)
                networkState(productListingUiState.networkState)
                retryCallback(OnClickListener {

                })
            }
        }
        buildMoreProductButtonUi()
    }

    private fun buildMoreProductButtonUi() {
        if (productListingUiState.networkState == NetworkState.LOADED &&
            productListingUiState.hasMore
        ) {
            TagMoreThreadPostViewModel_()
                .id("more-product")
                .onMoreClickCallback(OnClickListener {
                    onMoreProductClicked()
                })
                .addTo(this)
        }
    }

    private fun shouldHideVoteUi(threadUiState: ThreadUiState) =
        (!threadUiState.isTechnician && threadUiState.tagVoteOptions.isEmpty()) ||
                (threadUiState.tagVoteOptions.isEmpty() && threadUiState.hasApprovedTag)

    fun setProductPagedList(list: PagedList<Product>) {
        submitList(list)
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