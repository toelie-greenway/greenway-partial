package com.greenwaymyanmar.common.feature.tag.presentation.voting

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagsListingUseCase
import com.greenwaymyanmar.common.feature.tag.domain.usecases.GetTagsListingUseCase.GetTagsListingRequest
import com.greenwaymyanmar.common.feature.tag.domain.usecases.SaveThreadTagVoteUseCase
import com.greenwaymyanmar.common.feature.tag.domain.usecases.SaveThreadTagVoteUseCase.SaveThreadTagVoteRequest
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.ValidationResult
import greenway_myanmar.org.common.domain.entities.getDataOrThrow
import greenway_myanmar.org.common.domain.entities.getErrorOrNull
import greenway_myanmar.org.common.domain.entities.hasError
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VotingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTagsListingUseCase: GetTagsListingUseCase,
    private val saveThreadTagVoteUseCase: SaveThreadTagVoteUseCase
) : ViewModel() {

    private val args = VotingFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _uiState = MutableStateFlow(VotingUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: VotingUiState
        get() = uiState.value

    private val result: LiveData<Listing<Tag>> = combine(
        uiState.mapNotNull { it.category },
        uiState.map { it.searchQuery },
        ::Pair
    ).distinctUntilChanged()
        .map { (category, query) ->
            getTagsListingUseCase(
                GetTagsListingRequest(
                    categoryId = category.id,
                    query = query
                )
            )
        }
        .asLiveData()

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
                currentVotedTag = args.votedTags.firstOrNull { tag -> tag.isVoted },
                previousVotedTag = args.votedTags.firstOrNull { tag -> tag.isVoted }
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
            is VotingEvent.OnSubmit -> {
                onSubmit()
            }
        }
    }

    private fun updateVote(votableTag: UiVotableTag) {
        _uiState.update { currentUiState ->
            val newVotedTag = if (currentUiState.currentVotedTag?.tag?.id == votableTag.tag.id) {
                null
            } else {
                votableTag
            }

            currentUiState.copy(
                currentVotedTag = newVotedTag
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

    private fun onSubmit() {
        // validate inputs
        val tagValidationResult =
            validateTag(currentUiState.currentVotedTag, currentUiState.previousVotedTag)

        // set/reset error
        _uiState.value = currentUiState.copy(
            tagError = tagValidationResult.getErrorOrNull(),
        )

        // return if there is any error
        if (hasError(
                tagValidationResult,
            )
        ) {
            return
        }

        // collect result
        val votableTag = tagValidationResult.getDataOrThrow()
        val categoryId = currentUiState.customCategory?.id ?: currentUiState.category?.id.orEmpty()
        saveThreadTagVote(
            SaveThreadTagVoteRequest(
                tagId = votableTag?.tag?.id.orEmpty(),
                threadId = args.contentId,
                categoryId = categoryId,
                previousVotedOptionId = currentUiState.previousVotedTag?.id.orEmpty()
            )
        )
    }

    private fun saveThreadTagVote(request: SaveThreadTagVoteRequest) {
        viewModelScope.launch {
            try {
                updateSubmittingLoadingState(LoadingState.Loading)
                val success = saveThreadTagVoteUseCase(request)
                updateSubmittingLoadingState(
                    LoadingState.Success(success)
                )
            } catch (e: Exception) {
                updateSubmittingLoadingState(LoadingState.Error(e))
            }
        }
    }

    private fun updateSubmittingLoadingState(state: LoadingState<Boolean>) {
        _uiState.update { it.copy(submittingLoadingState = state) }
    }

    private fun validateTag(
        tag: UiVotableTag?,
        previousVotedTag: UiVotableTag?
    ): ValidationResult<UiVotableTag?> {
        return if (tag == null && previousVotedTag == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(tag)
        }
    }

}