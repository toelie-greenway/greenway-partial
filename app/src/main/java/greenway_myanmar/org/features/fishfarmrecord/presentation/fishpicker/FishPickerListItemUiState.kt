package greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

data class FishPickerListItemUiState(
    val fish: UiFish,
    val checked: Boolean
)