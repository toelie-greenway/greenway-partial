package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import com.greenwaymyanmar.core.data.json.serializers.BigDecimalAsString
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Expense
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputExpense
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.extensions.toBigDecimalOrNull
import greenway_myanmar.org.util.toInstantOrNow
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
    val inputs: List<NetworkFarmInputExpense>? = null,
    val general_expense: Double? = null,
    val sub_category: NetworkExpenseCategory? = null
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

fun NetworkExpense.asDomainModel() = Expense(
    id = id,
    date = date.toInstantOrNow(),
    labourQuantity = labour_qty,
    labourCost = labour_cost.toBigDecimalOrNull(),
    familyQuantity = family_qty,
    familyCost = family_cost.toBigDecimalOrNull(),
    machineryCost = machinery_cost.toBigDecimalOrNull(),
    totalCost = total_cost.toBigDecimalOrNull(),
    photos = photos,
    remark = remark,
    inputs = inputs.orEmpty().map {
        it.asDomainModel()
    },
    generalExpense = general_expense.toBigDecimalOrNull(),
    generalExpenseCategory = sub_category?.asDomainModel()
)

fun NetworkFarmInputExpense.asDomainModel() = FarmInputExpense(
    productId = product_id.orEmpty(),
    productName = product_name.orEmpty(),
    productThumbnail = product_thumbnail.orEmpty(),
    amount = quantity.orZero(),
    unit = unit.orEmpty(),
    unitPrice = unit_price.orZero(),
    fingerlingWeight = estimated_weight,
    fingerlingSize = estimated_size,
    fingerlingAge = fingerling_age,
    totalCost = total_cost.orZero()
)