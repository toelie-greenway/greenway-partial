package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import kotlinx.serialization.Serializable

@Serializable
data class NetworkExpenseCategory(
    val id: String? = "",
    val title: String? = "",
    val is_harvesting: Boolean? = false,
    val is_general: Boolean? = false,
    val order: Int? = -1,
)

fun NetworkExpenseCategory.asDomainModel() = ExpenseCategory(
    id = id.orEmpty(),
    name = title.orEmpty(),
    isHarvesting = is_harvesting ?: false,
    isGeneralExpenseCategory = is_general ?: false
)