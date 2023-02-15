package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmStreamUseCase.GetFarmRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeason
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FarmDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, getFarmStreamUseCase: GetFarmStreamUseCase
) : ViewModel() {

    private val farmIdStream = MutableStateFlow("")
    private val farmId = FarmDetailFragmentArgs.fromSavedStateHandle(savedStateHandle).farmId

    private val _uiState = MutableStateFlow(FarmDetailUiState())
    val uiState = _uiState.asStateFlow()

    val currentUiState: FarmDetailUiState
        get() = uiState.value

    val farmUiState: StateFlow<FarmUiState> =
        farmIdStream.flatMapLatest { farmId ->
            farmUiStateStream(
                farmId, getFarmStreamUseCase
            )
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    init {
        farmIdStream.value = farmId
        viewModelScope.launch {
            farmUiState.collect { farmUiState ->
                val openingSeason = (farmUiState as? LoadingState.Success)?.data?.ongoingSeason
                _uiState.update {
                    it.copy(
                        openingSeason = if (openingSeason != null) {
                            UiSeason.fromDomain(openingSeason)
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }

    fun getSeasonId() = currentUiState.openingSeason?.id
    fun getFarmId() = farmId
}

private fun farmUiStateStream(
    farmId: String, getFarmStreamUseCase: GetFarmStreamUseCase
): Flow<FarmUiState> {
    return if (farmId.isEmpty()) {
        emptyFlow()
    } else {
        getFarmStreamUseCase(GetFarmRequest(farmId)).catch { }.map { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data != null) {
                        LoadingState.Success(UiFarm.fromDomain(result.data))
                    } else {
                        LoadingState.Empty()
                    }
                }
                is Result.Error -> {
                    LoadingState.Error(result.exception)
                }
                Result.Loading -> {
                    LoadingState.Loading
                }
            }
        }
    }
}