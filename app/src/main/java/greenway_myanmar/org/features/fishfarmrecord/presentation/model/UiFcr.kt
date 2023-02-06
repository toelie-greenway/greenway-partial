package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import java.math.BigDecimal

data class UiFcr(
    val fish: UiFish,
    val feedWeight: BigDecimal,
    val gainWeight: BigDecimal,
)