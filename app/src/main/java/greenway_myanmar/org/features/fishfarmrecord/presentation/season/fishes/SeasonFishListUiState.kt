package greenway_myanmar.org.features.fishfarmrecord.presentation.season.fishes

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

data class SeasonFishListUiState(
    val fishes: List<UiFish> = emptyList()
)