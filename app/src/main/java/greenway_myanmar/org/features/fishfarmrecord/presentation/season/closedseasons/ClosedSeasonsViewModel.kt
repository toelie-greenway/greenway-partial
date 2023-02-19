package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetClosedSeasonsStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)

@HiltViewModel
class ClosedSeasonsViewModel @Inject constructor(
    private val getClosedSeasonsStreamUseCase: GetClosedSeasonsStreamUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val farmUiState = MutableStateFlow<FarmUiState>(LoadingState.Idle)
    private val query = MutableStateFlow(Query.Empty)

    val seasons: StateFlow<ClosedSeasonListUiState> = query.flatMapLatest { q ->
        q.ifExists { farmId ->
            closedSeasonsStream(
                farmId,
                farmUiState,
                getClosedSeasonsStreamUseCase
            )
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    fun handleEvent(event: ClosedSeasonsEvent) {
        when (event) {
            is ClosedSeasonsEvent.OnFarmIdChanged -> {
                updateFarmId(event.farmId)
            }
            is ClosedSeasonsEvent.OnFarmChanged -> {
                updateFarm(event.farmUiState)
            }
        }
    }

    private fun updateFarmId(farmId: String) {
        query.value = Query(farmId)
    }

    private fun updateFarm(farmUiState: LoadingState<UiFarm>) {
        this.farmUiState.value = farmUiState
    }

    data class Query(
        val farmId: String
    ) {
        fun <T> ifExists(f: (String) -> Flow<T>): Flow<T> {
            return if (farmId.isEmpty()) {
                emptyFlow()
            } else {
                f(farmId)
            }
        }

        companion object {
            val Empty = Query(farmId = "")
        }
    }
}


private fun closedSeasonsStream(
    farmId: String,
    farmUiStateStream: Flow<FarmUiState>,
    getClosedSeasonsStreamUseCase: GetClosedSeasonsStreamUseCase
): Flow<ClosedSeasonListUiState> {

    return combine(
        farmUiStateStream,
        getClosedSeasonsStreamUseCase(
            GetClosedSeasonsStreamUseCase.GetClosedSeasonsRequest(
                farmId = farmId
            )
        ),
        ::Pair
    )
        .map { (farmUiState, closedSeasonsResult) ->
            when {
                closedSeasonsResult is Result.Success && farmUiState is LoadingState.Success -> {
                    LoadingState.Success(
                        data = closedSeasonsResult.data.map {
                            ClosedSeasonListItemUiState.from(
                                domainModel = it,
                                farmName = farmUiState.data.name,
                                farmArea = it.farmMeasurement.area,
                                farmImages = farmUiState.data.images
                            )
                        }
                    )
                }
                closedSeasonsResult is Result.Error || farmUiState is LoadingState.Error -> {
                    if (closedSeasonsResult is Result.Error) {
                        LoadingState.Error(closedSeasonsResult.exception)
                    } else if (farmUiState is LoadingState.Error) {
                        LoadingState.Error(farmUiState.exception)
                    } else {
                        LoadingState.Error()
                    }
                }
                closedSeasonsResult is Result.Loading && farmUiState is LoadingState.Loading -> {
                    LoadingState.Loading
                }
                else -> {
                    LoadingState.Idle
                }
            }
        }
}