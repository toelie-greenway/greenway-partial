package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseByCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.util.toInstantOrNull
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
    name = title.orEmpty(),
    isHarvesting = is_harvesting == false
)

fun NetworkExpenseCategory.asExpenseByCategoryDomainModel() = ExpenseByCategory(
    category = asDomainModel(),
    totalExpenses = if (total_cost == null) BigDecimal.ZERO else BigDecimal.valueOf(total_cost),
    lastRecordDate = last_cost_created_date.toInstantOrNull()
)