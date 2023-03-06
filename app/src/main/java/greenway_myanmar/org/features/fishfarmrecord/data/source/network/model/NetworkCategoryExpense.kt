package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import greenway_myanmar.org.util.toInstantOrNow
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategoryExpense(
    val id: String? = "",
    val title: String? = "",
    val is_harvesting: Boolean? = false,
    val is_general: Boolean? = false,
    val order: Int? = -1,
    val total_cost: Double? = null,
    val last_cost_created_date: String? = null,
    val expenses: List<NetworkExpense>? = null
)

fun NetworkCategoryExpense.asDomainModel() = CategoryExpense(
    category = this.asExpenseCategoryDomainModel(),
    totalExpenses = total_cost.toBigDecimalOrZero(),
    lastRecordDate = last_cost_created_date.toInstantOrNow(),
    expenses = expenses.orEmpty().map {
        it.asDomainModel()
    }
)

fun NetworkCategoryExpense.asExpenseCategoryDomainModel() = ExpenseCategory(
    id = id.orEmpty(),
    name = title.orEmpty(),
    isHarvesting = is_harvesting ?: false,
    isGeneralExpenseCategory = is_general ?: false
)