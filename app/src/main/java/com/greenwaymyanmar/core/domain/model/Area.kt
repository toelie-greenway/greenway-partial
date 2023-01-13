package com.greenwaymyanmar.core.domain.model

import java.math.BigDecimal

sealed class Area(open val value: BigDecimal, val unit: String) {
    data class Acre(override val value: BigDecimal) : Area(value, "acre")

    companion object {
        val Empty = Area.Acre(BigDecimal(-1))
    }
}