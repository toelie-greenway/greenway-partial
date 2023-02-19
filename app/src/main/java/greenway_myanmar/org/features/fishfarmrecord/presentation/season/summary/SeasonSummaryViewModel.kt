package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetSeasonSummaryStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetSeasonSummaryStreamUseCase.GetSeasonSummaryRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeasonSummary
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SeasonSummaryViewModel @Inject constructor(
    getSeasonSummaryStreamUseCase: GetSeasonSummaryStreamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = SeasonSummaryFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val _query: MutableStateFlow<Query> = MutableStateFlow(Query.Empty)
    val seasonSummary: StateFlow<LoadingState<UiSeasonSummary>> = _query.flatMapLatest {
        it.ifExists { farmId, seasonId ->
            seasonSummaryStream(
                farmId = farmId,
                seasonId = seasonId,
                getSeasonSummaryStreamUseCase = getSeasonSummaryStreamUseCase
            )
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    init {
        _query.value = Query(
            farmId = args.farmId,
            seasonId = args.seasonId
        )
    }

    data class Query constructor(val farmId: String, val seasonId: String) {
        fun <T> ifExists(f: (String, String) -> Flow<T>): Flow<T> {
            return if (farmId.isBlank() && seasonId.isBlank()) {
                emptyFlow()
            } else {
                f(farmId, seasonId)
            }
        }

        companion object {
            val Empty = Query("", "")
        }
    }
}

private fun seasonSummaryStream(
    farmId: String,
    seasonId: String,
    getSeasonSummaryStreamUseCase: GetSeasonSummaryStreamUseCase
): Flow<LoadingState<UiSeasonSummary>> =
    getSeasonSummaryStreamUseCase(
        GetSeasonSummaryRequest(
            farmId = farmId,
            seasonId = seasonId
        )
    )
        .map { result ->
            Timber.d("Map result: $result")
            when (result) {
                is Result.Error -> {
                    LoadingState.Error(result.exception)
                }
                Result.Loading -> {
                    LoadingState.Loading
                }
                is Result.Success -> {
                    LoadingState.Success(
                        UiSeasonSummary.fromDomainModel(
                            result.data
                        )
                    )
                }
            }
        }