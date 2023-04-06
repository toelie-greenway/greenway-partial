package com.greenwaymyanmar.common.feature.tag.presentation.voting

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagsListingUseCase
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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

    private val result: LiveData<Listing<Tag>> = combine(
        uiState.map { it.category },
        uiState.map { it.searchQuery },
        ::Pair
    ).distinctUntilChanged()
        .map { (category, query) ->
            getTagsListingUseCase()
        }.asLiveData()

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
            )
        }
    }

    fun handleEvent(event: VotingEvent) {
        when (event) {
            is VotingEvent.OnVoteChanged -> {
                updateVote(event.votableTag)
            }
            is VotingEvent.OnQueryChanged -> {
                updateQuery(event.query)
            }
            is VotingEvent.OnCustomCategoryChanged -> {
                updateCustomCategory(event.category)
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

    private fun updateQuery(query: String?) {
        _uiState.update {
            it.copy(
                searchQuery = query.orEmpty()
            )
        }
    }

    private fun updateCustomCategory(category: UiCategory?) {
        _uiState.update {
            it.copy(
                customCategory = category
            )
        }
    }

}