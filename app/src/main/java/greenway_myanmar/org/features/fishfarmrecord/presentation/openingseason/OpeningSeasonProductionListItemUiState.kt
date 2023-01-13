package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import java.math.BigDecimal
import java.time.Instant

data class OpeningSeasonProductionListItemUiState(
    val lastRecordDate: Instant? = null,
    val totalIncome: BigDecimal
) {
    val hasRecord: Boolean = lastRecordDate != null

    companion object {
        val Empty = OpeningSeasonProductionListItemUiState(totalIncome = BigDecimal.ZERO)
    }
}
