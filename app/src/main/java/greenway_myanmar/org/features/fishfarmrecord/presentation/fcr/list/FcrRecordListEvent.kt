package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState

sealed class FcrRecordListEvent {
    data class OnSeasonIdChanged(val seasonId: String): FcrRecordListEvent()
    data class OnFarmChanged(val farmUiState: FarmUiState): FcrRecordListEvent()
}
