package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import kotlinx.datetime.Instant
import java.math.BigDecimal

data class FcrRecordListItemUiState(
    val id: String,
    val date: Instant,
    val calculatedRatio: BigDecimal,
    val totalFeedWeight: BigDecimal,
    val totalGainWeight: BigDecimal
)