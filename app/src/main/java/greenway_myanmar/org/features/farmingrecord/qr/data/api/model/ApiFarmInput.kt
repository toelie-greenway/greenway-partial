package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmInput
import java.math.BigDecimal

data class ApiFarmInput(
    @SerializedName("amount_per_unit")
    val amountPerUnit: BigDecimal? = null,
    @SerializedName("amount_unit")
    val amountUnit: String? = null,
    @SerializedName("expense_id")
    val expenseId: Int? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("product_category_id")
    val productCategoryId: String? = null,
    @SerializedName("product_category_name")
    val productCategoryName: String? = null,
    @SerializedName("product_id")
    val productId: String? = null,
    @SerializedName("product_name")
    val productName: String? = null,
    @SerializedName("quantity")
    val quantity: Double? = null,
    @SerializedName("total_cost")
    val totalCost: BigDecimal? = null,
    @SerializedName("unit")
    val unit: String? = null,
    @SerializedName("unit_price")
    val unitPrice: BigDecimal? = null,
) {
    fun toDomain() = FarmInput(
        id = id.orEmpty(),
        amountPerUnit = amountPerUnit ?: BigDecimal.ZERO,
        amountUnit = amountUnit.orEmpty(),
        unitPrice = unitPrice ?: BigDecimal.ZERO,
        unit = unit.orEmpty(),
        productCategoryId = productCategoryId.orEmpty(),
        productCategoryName = productCategoryName.orEmpty(),
        productId = productId.orEmpty(),
        productName = productName.orEmpty(),
        quantity = quantity ?: 0.0,
        totalCost = totalCost ?: BigDecimal.ZERO
    )
}