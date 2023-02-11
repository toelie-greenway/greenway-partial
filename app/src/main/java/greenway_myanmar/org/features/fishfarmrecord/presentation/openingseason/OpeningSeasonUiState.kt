package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import com.greenwaymyanmar.core.presentation.model.LoadingState

data class OpeningSeasonUiState(
    val seasonId: String = "",
)

typealias CategoryListUiState = LoadingState<List<OpeningSeasonCategoryListItemUiState>>