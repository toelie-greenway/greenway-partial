package greenway_myanmar.org.features.fishfarmrecord.presentation.closedseasons

import com.greenwaymyanmar.core.domain.model.Area
import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import java.math.BigDecimal
import java.time.Instant

data class ClosedSeasonListItemUiState(
    val id: String,
    val pondName: String,
    val area: UiArea?,
    val seasonName: String,
    val seasonStartDate: Instant,
    val totalExpenses: BigDecimal
) {
    companion object {
        fun from(domainModel: Season, pondName: String, area: Area) = ClosedSeasonListItemUiState(
            id = domainModel.id,
            pondName = pondName,
            seasonName = domainModel.name,
            seasonStartDate = domainModel.startDate,
            area = UiArea.fromDomain(area),
            totalExpenses = domainModel.totalExpenses
        )
    }
}