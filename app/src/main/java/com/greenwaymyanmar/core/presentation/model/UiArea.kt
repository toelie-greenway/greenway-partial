package com.greenwaymyanmar.core.presentation.model

import android.content.Context
import androidx.annotation.StringRes
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.domain.model.AREA_UNIT_ACRE
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.util.MyanmarZarConverter
import java.text.NumberFormat

data class UiArea(
    val value: Double,
    val unit: String,
    @StringRes val symbolResId: Int
) {

    companion object {
        fun acre(value: Double, unit: String = AREA_UNIT_ACRE) =
            UiArea(value, unit, R.string.area_acre_symbol)

        fun fromDomain(domainModel: Area) = UiArea(
            value = domainModel.value,
            unit = domainModel.unit,
            symbolResId = getSymbolResIdFor(domainModel.unit)
        )

        private fun getSymbolResIdFor(unit: String): Int {
            return when (unit) {
                AREA_UNIT_ACRE -> {
                    R.string.area_acre_symbol
                }
                else -> {
                    R.string.area_unsupported_symbol
                }
            }
        }
    }
}

fun UiArea.asString(context: Context): String {
    val numberFormat = NumberFormat.getInstance(MyanmarZarConverter.getLocale())
    numberFormat.maximumFractionDigits = 2
    return context.getString(
        R.string.formatted_area,
        numberFormat.format(this.value),
        context.getString(this.symbolResId)
    )
}

fun UiArea.asDomain(): Area = Area(
    value = this.value,
    unit = this.unit
)