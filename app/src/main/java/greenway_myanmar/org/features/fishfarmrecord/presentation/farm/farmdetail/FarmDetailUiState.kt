package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm

//data class FarmDetailUiState(
//    val farm: FarmUiState = LoadingState.Idle
//)

typealias FarmUiState = LoadingState<UiFarm>