package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import com.greenwaymyanmar.core.data.json.serializers.BigDecimalAsString
import kotlinx.serialization.Serializable

@Serializable
data class NetworkExpense(
    val id: String,
    val season_id: String? = null,
    val category: NetworkExpenseCategory? = null,
    val date: String,
    val labour_qty: Int? = null,
    val labour_cost: Double? = null,
    val family_qty: Int? = null,
    val family_cost: Double? = null,
    val machinery_cost: Double? = null,
    val total_cost: Double? = null,
    val photos: List<String>? = null,
    val remark: String? = null,
    val inputs: List<NetworkFarmInputExpense>? = null
)

@Serializable
data class NetworkFarmInputExpense(
    val product_id: String? = null,
    val product_name: String? = null,
    val product_thumbnail: String? = null,
    val quantity: BigDecimalAsString? = null,
    val unit: String? = null,
    val unit_price: BigDecimalAsString? = null,
    val total_cost: BigDecimalAsString? = null,
    val estimated_size: BigDecimalAsString? = null,
    val estimated_weight: BigDecimalAsString? = null,
    val fingerling_age: BigDecimalAsString? = null
)