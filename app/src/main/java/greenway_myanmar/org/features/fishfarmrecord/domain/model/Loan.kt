package greenway_myanmar.org.features.fishfarmrecord.domain.model

import java.math.BigDecimal

data class Loan(
    val amount: BigDecimal,
    val duration: Int,
    val organization: String,
    val remark: String? = null
)