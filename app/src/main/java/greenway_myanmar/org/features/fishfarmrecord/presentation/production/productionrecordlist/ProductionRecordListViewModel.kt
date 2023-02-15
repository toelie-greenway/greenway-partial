package greenway_myanmar.org.features.fishfarmrecord.presentation.production.productionrecordlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetProductionsStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiProductionRecord
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductionRecordListViewModel @Inject constructor(
    getProductionsStreamUseCase: GetProductionsStreamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = ProductionRecordListFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val query = MutableStateFlow(Query.Empty)
    private val productionRecordListLoadingState: StateFlow<LoadingState<List<UiProductionRecord>>> =
        query.flatMapLatest { q ->
            q.ifExists { seasonId ->
                productionRecordsStream(seasonId, getProductionsStreamUseCase)
            }
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    val uiState: StateFlow<ProductionRecordListUiState> = productionRecordListLoadingState.map {
        ProductionRecordListUiState.fromLoadingState(it)
    }.stateIn(viewModelScope, WhileViewSubscribed, ProductionRecordListUiState.Idle)

    init {
        query.value = Query(
            seasonId = args.seasonId
        )
    }

    data class Query(
        val seasonId: String? = null,
    ) {
        fun <T> ifExists(f: (String) -> Flow<T>): Flow<T> {
            return if (seasonId.isNullOrEmpty()) {
                emptyFlow()
            } else {
                f(seasonId)
            }
        }

        companion object {
            val Empty = Query()
        }
    }
}

private fun productionRecordsStream(
    seasonId: String,
    getProductionsStreamUseCase: GetProductionsStreamUseCase
): Flow<LoadingState<List<UiProductionRecord>>> {
    return getProductionsStreamUseCase(
        GetProductionsStreamUseCase.GetProductionsRequest(
            seasonId = seasonId
        )
    ).catch {
        LoadingState.Error(it)
    }.map { result ->
        when (result) {
            is Result.Error -> {
                LoadingState.Error(result.exception)
            }
            Result.Loading -> {
                LoadingState.Loading
            }
            is Result.Success -> {
                LoadingState.Success(
                    data = result.data.map {
                        UiProductionRecord.fromDomainModel(it)
                    }
                )
            }
        }
    }
}