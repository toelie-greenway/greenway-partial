package greenway_myanmar.org.features.fishfarmrecord.domain.model

import java.math.BigDecimal

data class Fcr(
    val fish: Fish,
    val feedWeight: BigDecimal,
    val gainWeight: BigDecimal,
)