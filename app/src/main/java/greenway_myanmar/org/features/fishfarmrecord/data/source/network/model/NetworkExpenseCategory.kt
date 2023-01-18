package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkExpenseCategory(
    val id: String,
    val title: String = "",
    @SerialName("is_harvesting") val isHarvesting: Boolean = false,
    val order: Int = -1
)

fun NetworkExpenseCategory.asDomainModel() = ExpenseCategory(
    id = id,
    name = title
)