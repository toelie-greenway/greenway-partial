package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary

import androidx.annotation.ColorRes

data class SeasonSummaryItemUiState(
    val key: String,
    val label: String,
    val value: String?,
    @ColorRes val valueTextColorResId: Int? = null
)
