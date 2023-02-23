package greenway_myanmar.org.features.fishfarmrecord.presentation.production.productionrecordlist

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiProductionRecord
import java.math.BigDecimal

data class ProductionRecordListUiState(
    val totalPrice: BigDecimal = BigDecimal.ZERO,
    val productionRecords: List<UiProductionRecord> = emptyList(),
    val loadingState: LoadingState<Unit> = LoadingState.Idle
) {

    companion object {
        val Idle = ProductionRecordListUiState(loadingState = LoadingState.Idle)
        val Empty = ProductionRecordListUiState(loadingState = LoadingState.Empty())
        val Error = ProductionRecordListUiState(loadingState = LoadingState.Empty())
        val Loading = ProductionRecordListUiState(loadingState = LoadingState.Loading)

        fun fromLoadingState(state: LoadingState<List<UiProductionRecord>>) = when (state) {
            is LoadingState.Empty -> {
                ProductionRecordListUiState(loadingState = LoadingState.Empty())
            }
            is LoadingState.Error -> {
                ProductionRecordListUiState(loadingState = LoadingState.Error(state.exception))
            }
            LoadingState.Idle -> {
                ProductionRecordListUiState(loadingState = LoadingState.Idle)
            }
            LoadingState.Loading -> {
                ProductionRecordListUiState(loadingState = LoadingState.Loading)
            }
            is LoadingState.Success -> {
                val data = state.data
                ProductionRecordListUiState(
                    totalPrice = data.sumOf { it.totalPrice },
                    productionRecords = data,
                    loadingState = LoadingState.Success(Unit)
                )
            }
        }
    }
}