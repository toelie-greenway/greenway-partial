package com.greenwaymyanmar.common.feature.tag.presentation.voting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.greenwaymyanmar.common.feature.tag.presentation.model.UiVotableTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class VotingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = VotingFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _uiState = MutableStateFlow(VotingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                category = "ပိုးမွှားရောဂါ",
                crop = "ထန်းသီး",
                votableTags = args.votedTags.toList()
            )
        }
    }

    fun handleEvent(event: VotingEvent) {
        when (event) {
            is VotingEvent.OnToggleVote -> {
                updateVote(event.votableTag)
            }
        }
    }

    private fun updateVote(votableTag: UiVotableTag) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                votableTags = currentUiState.votableTags.map {
                    it.copy(
                        isVoted = if (votableTag.tag.id == it.tag.id) {
                            !it.isVoted
                        } else {
                            false
                        }
                    )
                }
            )
        }
    }
}