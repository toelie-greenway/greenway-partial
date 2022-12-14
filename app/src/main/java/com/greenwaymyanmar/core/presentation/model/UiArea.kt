package com.greenwaymyanmar.core.presentation.model

import android.content.Context
import androidx.annotation.StringRes
import com.greenwaymyanmar.core.domain.model.Area
import greenway_myanmar.org.R
import greenway_myanmar.org.util.MyanmarZarConverter
import java.math.BigDecimal
import java.text.NumberFormat

sealed class UiArea(
    open val value: BigDecimal,
    open val unit: String,
    @StringRes val symbolResId: Int
) {
    data class Acre(override val value: BigDecimal, override val unit: String) :
        UiArea(value, unit, R.string.area_acre_symbol)

    companion object {
        fun fromDomain(domainEntity: Area): UiArea {
            return when (domainEntity) {
                is Area.Acre -> Acre(domainEntity.value, domainEntity.unit)
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