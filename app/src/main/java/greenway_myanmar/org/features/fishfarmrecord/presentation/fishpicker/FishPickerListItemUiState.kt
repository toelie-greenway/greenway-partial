package greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

data class FishPickerListItemUiState(
    val fish: UiFish,
    val checked: Boolean
)

typealias FishesUiState = LoadingState<List<FishPickerListItemUiState>>
