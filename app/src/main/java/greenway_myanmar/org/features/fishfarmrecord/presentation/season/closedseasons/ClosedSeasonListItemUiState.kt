package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class ClosedSeasonListItemUiState(
    val id: String,
    val farmName: String,
    val area: UiArea?,
    val seasonName: String,
    val seasonStartDate: Instant,
    val totalExpenses: BigDecimal
) {
    companion object {
        fun from(domainModel: Season, farmName: String, farmArea: Area?) = ClosedSeasonListItemUiState(
            id = domainModel.id,
            farmName = farmName,
            seasonName = domainModel.name,
            seasonStartDate = domainModel.startDate,
            area = if (farmArea != null) UiArea.fromDomain(farmArea) else null,
            totalExpenses = domainModel.totalExpenses
        )
    }
}