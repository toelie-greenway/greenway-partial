package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategoryExpense(
    val id: String? = "",
    val title: String? = "",
    val is_harvesting: Boolean? = false,
    val order: Int? = -1,
    val total_cost: Double? = null,
    val last_cost_created_date: String? = null,
    val expenses: List<NetworkExpense>? = null
)