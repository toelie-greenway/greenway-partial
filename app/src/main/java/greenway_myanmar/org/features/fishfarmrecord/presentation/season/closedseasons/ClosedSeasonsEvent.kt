package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState

sealed class ClosedSeasonsEvent {
    data class OnFarmIdChanged(val farmId: String): ClosedSeasonsEvent()
    data class OnFarmChanged(val farmUiState: FarmUiState): ClosedSeasonsEvent()

}
