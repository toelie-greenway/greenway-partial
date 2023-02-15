package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.cropincomelist

import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState

sealed class CropIncomeListEvent {
    data class OnFarmChanged(val farmUiState: FarmUiState): CropIncomeListEvent()
}
