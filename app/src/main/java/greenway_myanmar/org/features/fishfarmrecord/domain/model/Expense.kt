package greenway_myanmar.org.features.fishfarmrecord.domain.model

import kotlinx.datetime.Instant
import java.math.BigDecimal

data class Expense(
    val id: String,
    val date: Instant,
    val labourQuantity: Int? = null,
    val labourCost: BigDecimal? = null,
    val familyQuantity: Int? = null,
    val familyCost: BigDecimal? = null,
    val machineryCost: BigDecimal? = null,
    val totalCost: BigDecimal? = null,
    val photos: List<String>? = null,
    val remark: String? = null,
    val inputs: List<FarmInputExpense>? = null,
    val generalExpense: BigDecimal?,
    val generalExpenseCategory: ExpenseCategory?
)