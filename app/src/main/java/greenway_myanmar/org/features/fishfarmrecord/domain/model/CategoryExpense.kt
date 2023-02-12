package greenway_myanmar.org.features.fishfarmrecord.domain.model

import kotlinx.datetime.Instant
import java.math.BigDecimal

data class CategoryExpense(
    val category: ExpenseCategory,
    val totalExpenses: BigDecimal,
    val lastRecordDate: Instant? = null,
    val expenses: List<Expense> = emptyList()
)
