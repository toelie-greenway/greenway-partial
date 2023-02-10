package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkExpense(
    val id: String,
    val season_id: String,
    val category: NetworkExpenseCategory,
    val date: String,
    val labour_qty: Int? = null,
    val labour_cost: Double? = null,
    val family_qty: Int? = null,
    val family_cost: Double? = null,
    val machinery_cost: Double? = null,
    val photos: List<String>? = null,
    val remark: String? = null
)
