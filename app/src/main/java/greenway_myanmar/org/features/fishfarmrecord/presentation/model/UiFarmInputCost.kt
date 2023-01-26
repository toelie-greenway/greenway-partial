package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import java.math.BigDecimal

data class UiFarmInputCost(
    val productId: String,
    val productName: String,
    val productThumbnail: String,
    var amount: Double = 0.0,
    var unit: String = "",
    var unitPrice: Int,
    var amountPerPackage: Double? = null,
    var packageUnit: String? = null,
    var totalCost: BigDecimal
)