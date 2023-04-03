package com.greenwaymyanmar.common.feature.tag.presentation.voting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategoryType
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagsListingUseCase
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiTag
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class VotingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTagsListingUseCase: GetTagsListingUseCase
) : ViewModel() {

    private val args = VotingFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _uiState = MutableStateFlow(VotingUiState())
    val uiState = _uiState.asStateFlow()

    private val result = MutableLiveData<Listing<Tag>>()
    val tags =
        Transformations.switchMap(result) { it.pagedList }
    val networkState: LiveData<NetworkState> =
        Transformations.switchMap(result) { it.networkState }
    val hasMore: LiveData<Boolean> = Transformations.switchMap(result) { it.hasMore }

    init {
        _uiState.update {
            it.copy(
                category = args.category,
                cropOrAnimalName = args.cropOrAnimalName,
                voteOptions = args.votedTags.toList(),
                tags = LoadingState.Success(
                    listOf(
                        UiVotableTag(
                            tag = UiTag(
                                id = "1",
                                name = "ပင်စည်ပုပ်ရောဂါ",
                                category = UiCategory(
                                    id = "1",
                                    name = "ပိုးမွှားရောဂါ",
                                    imageUrl = "https://picsum.photos/id/12/960/540",
                                    type = UiCategoryType.Agri
                                ),
                                imageUrls = listOf(
                                    "https://picsum.photos/id/210/960/540"
                                )
                            ),
                            voteCount = 0,
                            isVoted = true,
                            suggestedCategory = null
                        ),
                        UiVotableTag(
                            tag = UiTag(
                                id = "2",
                                name = "ပင်စည်ပုပ်ရောဂါ",
                                category = UiCategory(
                                    id = "1",
                                    name = "ပိုးမွှားရောဂါ",
                                    imageUrl = "https://picsum.photos/id/12/960/540",
                                    type = UiCategoryType.Agri
                                ),
                                imageUrls = listOf(
                                    "https://picsum.photos/id/210/960/540"
                                )
                            ),
                            voteCount = 0,
                            isVoted = false,
                            suggestedCategory = null
                        )
                    )
                )
            )
        }
        result.value = getTagsListingUseCase()
    }

    fun handleEvent(event: VotingEvent) {
        when (event) {
            is VotingEvent.OnVoteChanged -> {
                updateVote(event.votableTag)
            }
        }
    }

    private fun updateVote(votableTag: UiVotableTag) {
        _uiState.update { currentUiState ->
            val newVotedTag = if (currentUiState.votedTag?.tag?.id == votableTag.tag.id) {
                null
            } else {
                votableTag
            }

            currentUiState.copy(
                votedTag = newVotedTag
            )
        }
    }

    fun loadNextPage() {
        result.value?.loadNextPageCallback?.load()
    }
}