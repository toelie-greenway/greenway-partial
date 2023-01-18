package greenway_myanmar.org.features.fishfarmrecord.domain.model

import java.math.BigDecimal

sealed class Area(open val value: BigDecimal, val unit: String) {
    data class Acre(override val value: BigDecimal) : Area(value, "acre")

    companion object {
        val Empty = Acre(BigDecimal(-1))
    }
}