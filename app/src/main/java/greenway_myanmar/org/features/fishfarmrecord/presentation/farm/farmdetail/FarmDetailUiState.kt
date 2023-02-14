package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeason

data class FarmDetailUiState(
    val selectedTabPosition: Int = FarmDetailTabUiState.OpeningSeason.index,
    val openingSeason: UiSeason? = null
)

typealias FarmUiState = LoadingState<UiFarm>