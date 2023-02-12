package greenway_myanmar.org.features.fishfarmrecord.presentation.season.seasonend

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeasonEndReason

data class SeasonEndUiState(
    val farmId: String = "",
    val seasonId: String = "",
    val reason: UiSeasonEndReason? = null,
    val reasonError: Text? = null
)

typealias SeasonEndReasonsUiState = LoadingState<List<SeasonEndReasonListItemUiState>>
typealias SaveEndSeasonUiState = LoadingState<Unit>
