package com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.controller

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.common.feature.tag.presentation.tag.TagListingUiState
import com.greenwaymyanmar.common.feature.tag.presentation.voting.VotingUiState
import com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models.TagVotingVotableTagItemViewModel_
import com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models.tagVotingOptionsSubheaderView
import com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models.tagVotingTagsSubheaderView
import com.greenwaymyanmar.common.feature.tag.presentation.voting.epoxy.models.tagVotingVotableTagItemView
import com.greenwaymyanmar.common.ui.epoxy.model.listNetworkStateItemView
import greenway_myanmar.org.R
import greenway_myanmar.org.vo.NetworkState
import timber.log.Timber

class VotingController constructor(
    private val onVoteClicked: (UiVotableTag) -> Unit
) : PagedListEpoxyController<Tag>() {

    var uiState: VotingUiState? = null
        set(value) {
            Timber.d("uiState: $uiState")
            if (value != field) {
                field = value
                requestForcedModelBuild()
            }
        }

    private var listingUiState: TagListingUiState = TagListingUiState.Empty
        set(value) {
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

//    override fun buildModels(data: VotingUiState?) {
//        data?.let {
//
//
//            when (it.tags) {
//                LoadingState.Idle -> {
//                    /* no-op */
//                }
//                is LoadingState.Success -> {
//                    it.tags.data.forEach {
//                        tagVotingVotableTagItemView {
//                            id("tag-${it.tag.id}")
//                            votableTag(it)
//                        }
//                    }
//                }
//                is LoadingState.Empty,
//                is LoadingState.Error,
//                LoadingState.Loading -> {
//                    listLoadingStateView {
//                        id("tags-loading-state")
//                        loadingState(it.tags)
//                    }
//                }
//            }
//        }
//    }

    override fun buildItemModel(currentPosition: Int, item: Tag?): EpoxyModel<*> {
        if (item == null) return TagVotingVotableTagItemViewModel_()
            .id("tag-null-item")

        val tag = UiVotableTag.fromDomainModel(item)
        return TagVotingVotableTagItemViewModel_()
            .id("tag-${item.id}")
            .votableTag(
                tag.copy(
                    isVoted = isVotedTag(tag)
                )
            )
            .onVoteClickListener(View.OnClickListener {
                onVoteClicked(tag)
            })
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        val voteOptions = uiState?.voteOptions.orEmpty()
        if (voteOptions.isNotEmpty()) {
            tagVotingOptionsSubheaderView {
                id("vote-options-subheader")
            }

            voteOptions.forEach { votableTag ->
                tagVotingVotableTagItemView {
                    id("vote-option-${votableTag.tag.id}")
                    votableTag(
                        votableTag.copy(
                            isVoted = isVotedTag(votableTag)
                        )
                    )
                    onVoteClickListener(View.OnClickListener {
                        onVoteClicked(votableTag)
                    })
                }
            }
        }

        val customCategory = uiState?.customCategory
        tagVotingTagsSubheaderView {
            id("tags-subheader")
            category(customCategory)
        }

        super.addModels(models)

        if (hasExtraRow()) {
            listNetworkStateItemView {
                id("product-network-state")
                loadingView(R.layout.tag_voting_votable_tag_loading_view)
                networkState(listingUiState.networkState)
                retryCallback(View.OnClickListener {

                })
            }
        }
    }

    private fun isVotedTag(votableTag: UiVotableTag) =
        votableTag.tag.id == uiState?.votedTag?.tag?.id

    fun setNetworkState(networkState: NetworkState?) {
        listingUiState = listingUiState.copy(
            networkState = networkState
        )
    }

    fun setHasMore(hasMore: Boolean?) {
        Timber.d("setHasMore: $hasMore")
        listingUiState = listingUiState.copy(
            hasMore = hasMore == true
        )
    }

    private fun hasExtraRow(): Boolean {
        return listingUiState.networkState != NetworkState.LOADED
    }
}