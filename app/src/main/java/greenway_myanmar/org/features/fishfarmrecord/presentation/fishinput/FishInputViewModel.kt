package greenway_myanmar.org.features.fishfarmrecord.presentation.fishinput

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorsOrNull
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FishInputViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FishInputUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: FishInputUiState
        get() = uiState.value

    val selectedFish
        get() = uiState.value.fish

    private val _navigationEvents = Channel<FishInputNavigationEvent>(capacity = Channel.CONFLATED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun handleEvent(event: FishInputUiEvent) {
        when (event) {
            is FishInputUiEvent.OnFishChanged -> {
                updateFish(event.fish)
            }
            is FishInputUiEvent.OnSpeciesChanged -> {
                updateSpecies(event.species)
            }
            FishInputUiEvent.OnSubmit -> {
                onSubmit()
            }
            FishInputUiEvent.ResetFishValidationError -> {
                resetFishValidationError()
            }
        }
    }

    private fun updateFish(fish: UiFish) {
        _uiState.value = uiState.value.copy(fish = fish)
    }

    private fun updateSpecies(species: String) {
        _uiState.value = uiState.value.copy(species = species)
    }

    private fun onSubmit() {
        val fishValidationResult = validateFish(currentUiState.fish)

        _uiState.update {
            it.copy(
                fishValidationError = fishValidationResult.getErrorsOrNull()
            )
        }

        val hasError = listOf(fishValidationResult).any { !it.isSuccessful }
        if (hasError) {
            return
        }

        _navigationEvents.trySend(
            FishInputNavigationEvent.NavigateBackWithResult(
                fish = fishValidationResult.getDataOrThrow().copy(
                    species = currentUiState.species.orEmpty()
                )
            )
        )
    }

    private fun resetFishValidationError() {
        _uiState.value = currentUiState.copy(
            fishValidationError = null
        )
    }

    private fun validateFish(fish: UiFish?): ValidationResult<UiFish> {
        return if (fish == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(fish)
        }
    }
}
