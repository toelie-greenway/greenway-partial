package greenway_myanmar.org.features.fishfarmrecord.domain.model

import java.math.BigDecimal

data class FarmInputCost(
    val productId: String,
    val productName: String,
    val amount: Double = 0.0,
    val unit: String = "",
    val unitPrice: BigDecimal,
    val fingerlingWeight: BigDecimal? = null,
    val fingerlingSize: BigDecimal? = null,
    val fingerlingAge: BigDecimal? = null,
    val totalCost: BigDecimal
)