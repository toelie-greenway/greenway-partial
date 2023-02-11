package greenway_myanmar.org.features.fishfarmrecord.presentation.season.seasonend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.hasError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.EndSeasonUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetSeasonEndReasonsStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asDomainModel
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SeasonEndViewModel @Inject constructor(
    private val getSeasonEndReasonsStreamUseCase: GetSeasonEndReasonsStreamUseCase,
    private val endSeasonUseCase: EndSeasonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeasonEndUiState())
    val uiState = _uiState.asStateFlow()

    private val _saveEndSeasonUiState = MutableStateFlow<SaveEndSeasonUiState>(LoadingState.Idle)
    val saveEndSeasonUiState = _saveEndSeasonUiState.asStateFlow()

    private val currentUiState: SeasonEndUiState
        get() = uiState.value

    private val refreshSignal = MutableSharedFlow<Unit>()
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    val reasonsUiState: StateFlow<SeasonEndReasonsUiState> = loadDataSignal.flatMapLatest {
        seasonEndReasonsStream(
            uiState.map { it.reason }.distinctUntilChanged(),
            getSeasonEndReasonsStreamUseCase
        )
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    fun handleEvent(event: SeasonEndEvent) {
        when (event) {
            is SeasonEndEvent.OnReasonChanged -> {
                updateReason(event.item)
            }
            SeasonEndEvent.OnSubmit -> {
                onSubmit()
            }
            SeasonEndEvent.OnReasonErrorShown -> {
                clearReasonError()
            }
        }
    }

    private fun updateReason(item: SeasonEndReasonListItemUiState) {
        _uiState.update {
            it.copy(reason = item.item)
        }
    }

    private fun clearReasonError() {
        _uiState.update {
            it.copy(reasonError = null)
        }
    }

    private fun onSubmit() {
        // validate inputs
        val reasonValidationResult = validateReason(currentUiState.reason)

        // set/reset error
        _uiState.value = currentUiState.copy(
            reasonError = reasonValidationResult.getErrorOrNull(),
        )

        // return if there is any error
        if (hasError(
                reasonValidationResult,
            )
        ) {
            return
        }

        // collect result
        val reason = reasonValidationResult.getDataOrThrow()
        saveEndReason(
            EndSeasonUseCase.EndSeasonRequest(
                reason = reason.asDomainModel()
            )
        )
    }

    private fun saveEndReason(request: EndSeasonUseCase.EndSeasonRequest) {
        viewModelScope.launch {
            runCancellableCatching {
                _saveEndSeasonUiState.value = LoadingState.Loading
                endSeasonUseCase(request)
            }.onSuccess {
                _saveEndSeasonUiState.value = LoadingState.Success(Unit)
            }.onFailure {
                _saveEndSeasonUiState.value = LoadingState.Error(it.errorText())
            }
        }
    }

    private fun validateReason(reason: UiSeasonEndReason?): ValidationResult<UiSeasonEndReason> {
        return if (reason == null) {
            ValidationResult.Error(Text.ResourceText(R.string.ffr_season_end_error_reason_required))
        } else {
            ValidationResult.Success(reason)
        }
    }
}

private fun seasonEndReasonsStream(
    selectedReasonStream: Flow<UiSeasonEndReason?>,
    getSeasonEndReasonsStreamUseCase: GetSeasonEndReasonsStreamUseCase
): Flow<SeasonEndReasonsUiState> {

    return combine(
        selectedReasonStream,
        getSeasonEndReasonsStreamUseCase(),
        ::Pair
    ).catch {
        LoadingState.Error(it.errorText())
    }.map { (selectedReason, result) ->
        when (result) {
            is Result.Success -> {
                LoadingState.Success(
                    result.data.map {
                        SeasonEndReasonListItemUiState(
                            item = UiSeasonEndReason.fromDomainModel(it),
                            checked = it.id == selectedReason?.id
                        )
                    }
                )
            }
            is Result.Error -> {
                LoadingState.Error(result.exception.errorText())
            }
            Result.Loading -> {
                LoadingState.Loading
            }
        }
    }
}
