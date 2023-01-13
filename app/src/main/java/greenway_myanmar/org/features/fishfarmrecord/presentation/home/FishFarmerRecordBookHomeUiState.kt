package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import greenway_myanmar.org.common.domain.entities.Text

data class FishFarmerRecordBookHomeUiState(
    val isLoading: Boolean = true,
    val ponds: List<PondListItemUiState> = emptyList(),
    val error: Text? = null
)