package greenway_myanmar.org.features.farmingrecord.qr.domain.model

import java.math.BigDecimal

data class FarmInput(
    val id: String,
    val amountPerUnit: BigDecimal,
    val amountUnit: String,
    val productCategoryId: String,
    val productCategoryName: String,
    val productId: String,
    val productName: String,
    val quantity: Double,
    val totalCost: BigDecimal,
    val unit: String,
    val unitPrice: BigDecimal
)