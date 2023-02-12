package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState

sealed class OpeningSeasonEvent {
    data class OnSeasonIdChanged(val seasonId: String): OpeningSeasonEvent()
    data class OnFarmChanged(val farmUiState: FarmUiState): OpeningSeasonEvent()

}
