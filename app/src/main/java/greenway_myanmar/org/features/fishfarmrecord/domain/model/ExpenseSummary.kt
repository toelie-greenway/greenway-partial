package greenway_myanmar.org.features.fishfarmrecord.domain.model

import java.math.BigDecimal

data class ExpenseSummary(
    val totalExpense: BigDecimal,
    val categoryExpenses: List<CategoryExpense>
)