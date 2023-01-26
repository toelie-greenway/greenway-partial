package com.greenwaymyanmar.core.domain.model

data class UnitOfMeasurement(
    val unit: String
) {
    companion object {
        fun fromString(unitString: String) = UnitOfMeasurement(
            unit = unitString
        )
    }
}