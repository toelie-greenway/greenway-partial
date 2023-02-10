package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseByCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.util.toInstantOrNow
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class NetworkExpenseCategory(
    val id: String? = "",
    val title: String? = "",
    val is_harvesting: Boolean? = false,
    val order: Int? = -1,
    val total_cost: Double? = null,
    val last_cost_created_date: String? = null
)

fun NetworkExpenseCategory.asDomainModel() = ExpenseCategory(
    id = id.orEmpty(),
    name = title.orEmpty()
)

fun NetworkExpenseCategory.asExpenseByCategoryDomainModel() = ExpenseByCategory(
    category = asDomainModel(),
    totalExpenses = BigDecimal(total_cost?.toString().orEmpty()),
    lastRecordDate = last_cost_created_date.toInstantOrNow()
)