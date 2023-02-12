package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import com.greenwaymyanmar.core.data.json.serializers.BigDecimalAsString
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputExpense
import kotlinx.serialization.Serializable

@Serializable
data class FfrFarmInputExpenseEntity(
    val productId: String,
    val productName: String,
    val productThumbnail: String,
    val amount: BigDecimalAsString,
    val unit: String = "",
    val unitPrice: BigDecimalAsString,
    val fingerlingWeight: BigDecimalAsString? = null,
    val fingerlingSize: BigDecimalAsString? = null,
    val fingerlingAge: BigDecimalAsString? = null,
    val totalCost: BigDecimalAsString
)

fun FfrFarmInputExpenseEntity.asDomainModel() = FarmInputExpense(
    productId = productId,
    productName = productName,
    productThumbnail = productThumbnail,
    amount = amount,
    unit = unit,
    unitPrice = unitPrice,
    fingerlingWeight = fingerlingWeight,
    fingerlingSize = fingerlingSize,
    fingerlingAge = fingerlingAge,
    totalCost = totalCost
)