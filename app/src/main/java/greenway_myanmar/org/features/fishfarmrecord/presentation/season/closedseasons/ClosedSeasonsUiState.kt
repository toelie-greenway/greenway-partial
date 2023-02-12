package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import com.greenwaymyanmar.core.presentation.model.LoadingState

data class ClosedSeasonsUiState(
    val closedSeasons: List<ClosedSeasonListItemUiState> = emptyList(),
    val loadingState: LoadingState<Unit> = LoadingState.Idle
) {

    companion object {
        val Empty = ClosedSeasonsUiState()

        fun success(seasons: List<ClosedSeasonListItemUiState>) = ClosedSeasonsUiState(
            closedSeasons = seasons,
            loadingState = LoadingState.Success(Unit)
        )
    }
}

typealias ClosedSeasonListUiState = LoadingState<List<ClosedSeasonListItemUiState>>