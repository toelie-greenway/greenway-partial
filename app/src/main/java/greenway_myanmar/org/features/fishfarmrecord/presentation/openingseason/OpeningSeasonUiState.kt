package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import greenway_myanmar.org.common.domain.entities.Text

data class OpeningSeasonUiState(
    val isLoading: Boolean = false,
    val categories: List<OpeningSeasonCategoryListItemUiState> = emptyList(),
    val error: Text? = null,
    val isCloseableSeason: Boolean = false,
    val isProducible: Boolean = false,
    val isFcrRecordable: Boolean = false
)
