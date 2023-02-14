package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFcrRecordsStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FcrRecordListViewModel @Inject constructor(
    getFcrRecordsStreamUseCase: GetFcrRecordsStreamUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FcrRecordListUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: FcrRecordListUiState
        get() = uiState.value

    private val farmUiState = MutableStateFlow<FarmUiState>(LoadingState.Idle)

    val recordsUiState: StateFlow<RecordsUiState> = farmUiState.flatMapLatest { farmUiState ->
        fcrRecordsStream(farmUiState, getFcrRecordsStreamUseCase)
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    init {

    }

    fun handleEvent(event: FcrRecordListEvent) {
        when (event) {
            is FcrRecordListEvent.OnSeasonIdChanged -> {
                updateSeasonId(event.seasonId)
            }
            is FcrRecordListEvent.OnFarmChanged -> {
                updateFarm(event.farmUiState)
            }
        }
    }

    private fun updateFarm(farmUiState: LoadingState<UiFarm>) {
        this.farmUiState.value = farmUiState
    }

    private fun updateSeasonId(seasonId: String) {
        if (currentUiState.seasonId == seasonId) {
            return
        }
        _uiState.update {
            it.copy(seasonId = seasonId)
        }
    }
}

private fun fcrRecordsStream(
    farmUiState: FarmUiState,
    getFcrRecordsStreamUseCase: GetFcrRecordsStreamUseCase
): Flow<RecordsUiState> {
    return flow {
        emit(LoadingState.Loading)
        when (farmUiState) {
            is LoadingState.Success -> {
                val openingSeason = farmUiState.data.ongoingSeason
                if (openingSeason == null) {
                    emit(LoadingState.Empty(Text.ResourceText(R.string.ffr_farm_detail_label_no_ongoing_season)))
                } else {
                    emitAll(
                        loadFcrRecordsStream(openingSeason.id, getFcrRecordsStreamUseCase)
                    )
                }
            }
            LoadingState.Loading -> {
                emit(LoadingState.Loading)
            }
            else -> {
                emit(LoadingState.Idle)
            }
        }
    }
}

private fun loadFcrRecordsStream(
    seasonId: String,
    getFcrRecordsStreamUseCase: GetFcrRecordsStreamUseCase
) = getFcrRecordsStreamUseCase(
    GetFcrRecordsStreamUseCase.GetFcrRecordsRequest(seasonId)
)
    .catch {
        LoadingState.Empty(it.errorText())
    }.map { result ->
        when (result) {
            is Result.Error -> {
                LoadingState.Error(result.exception)
            }
            Result.Loading -> {
                LoadingState.Loading
            }
            is Result.Success -> {
                LoadingState.Success(result.data.map {
                    FcrRecordListItemUiState.fromDomainModel(it)
                })
            }
        }
    }