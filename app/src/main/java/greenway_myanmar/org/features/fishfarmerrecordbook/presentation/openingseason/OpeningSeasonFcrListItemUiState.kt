package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import java.math.BigDecimal
import java.time.Instant

data class OpeningSeasonFcrListItemUiState(
    val lastRecordDate: Instant? = null,
    val fcrRatio: BigDecimal
) {
    val hasRecord: Boolean = lastRecordDate != null

    companion object {
        val Empty = OpeningSeasonFcrListItemUiState(fcrRatio = BigDecimal.ZERO)
    }
}
