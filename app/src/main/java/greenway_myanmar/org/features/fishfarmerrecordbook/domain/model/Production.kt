package greenway_myanmar.org.features.fishfarmerrecordbook.domain.model

import java.math.BigDecimal
import java.time.Instant

data class Production(
    val totalIncome: BigDecimal,
    val lastRecordDate: Instant? = null
)