package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmInput
import java.math.BigDecimal

data class UiFarmInput(
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
) {
    companion object {
        fun fromDomain(domainEntity: FarmInput) = UiFarmInput(
            id = domainEntity.id,
            amountPerUnit = domainEntity.amountPerUnit,
            amountUnit = domainEntity.amountUnit,
            productCategoryId = domainEntity.productCategoryId,
            productCategoryName = domainEntity.productCategoryName,
            productId = domainEntity.productId,
            productName = domainEntity.productName,
            quantity = domainEntity.quantity,
            totalCost = domainEntity.totalCost,
            unit = domainEntity.unit,
            unitPrice = domainEntity.unitPrice
        )
    }
}