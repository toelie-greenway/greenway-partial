package greenway_myanmar.org.features.fishfarmrecord.presentation.closedseasons

import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class ClosedSeasonListItemUiState(
    val id: String,
    val pondName: String,
    val area: UiArea?,
    val seasonName: String,
    val seasonStartDate: Instant,
    val totalExpenses: BigDecimal
) {
    companion object {
        fun from(domainModel: Season, pondName: String, area: Area?) = ClosedSeasonListItemUiState(
            id = domainModel.id,
            pondName = pondName,
            seasonName = domainModel.name,
            seasonStartDate = domainModel.startDate,
            area = if (area != null) UiArea.fromDomain(area) else null,
            totalExpenses = domainModel.totalExpenses
        )
    }
}