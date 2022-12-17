package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import java.math.BigDecimal
import java.time.Instant

data class OpeningSeasonProductionListItemUiState(
    val lastRecordDate: Instant? = null,
    val totalIncome: BigDecimal
) {
    val hasRecord: Boolean = lastRecordDate != null
}
