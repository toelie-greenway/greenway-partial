package greenway_myanmar.org.features.fishfarmrecord.domain.model

import java.math.BigDecimal
import java.time.Instant

data class ExpenseCategoryWithTotalExpenses(
    val category: ExpenseCategory,
    val totalExpenses: BigDecimal,
    val lastRecordDate: Instant? = null
)
