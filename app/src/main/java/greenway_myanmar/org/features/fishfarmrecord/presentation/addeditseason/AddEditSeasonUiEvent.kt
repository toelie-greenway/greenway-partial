package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

sealed class AddEditSeasonUiEvent {
    data class OnFishListChanged(val fishes: List<UiFish>): AddEditSeasonUiEvent()
    data class OnFishAdded(val fish: UiFish): AddEditSeasonUiEvent()
}