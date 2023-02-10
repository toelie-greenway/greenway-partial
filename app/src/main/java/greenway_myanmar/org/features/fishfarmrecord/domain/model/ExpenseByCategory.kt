package greenway_myanmar.org.features.fishfarmrecord.domain.model

import kotlinx.datetime.Instant
import java.math.BigDecimal

data class ExpenseByCategory(
    val category: ExpenseCategory,
    val totalExpenses: BigDecimal,
    val lastRecordDate: Instant? = null
)
