package greenway_myanmar.org.features.fishfarmrecord.domain.model

import kotlinx.datetime.Instant
import java.math.BigDecimal

data class ProductionRecordSummary(
    val totalWeight: BigDecimal,
    val totalIncome: BigDecimal,
    val lastRecordDate: Instant,
    val productionRecords: List<ProductionRecord>
)