package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import com.greenwaymyanmar.core.presentation.model.LoadingState


data class FcrRecordListUiState(
    val seasonId: String = "",
)

typealias RecordsUiState = LoadingState<List<FcrRecordListItemUiState>>