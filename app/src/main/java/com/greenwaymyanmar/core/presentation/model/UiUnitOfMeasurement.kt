package com.greenwaymyanmar.core.presentation.model

import android.os.Parcelable
import com.greenwaymyanmar.core.domain.model.UnitOfMeasurement
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiUnitOfMeasurement(
    val unit: String
): Parcelable {
    companion object {
        fun fromDomainModel(domainModel: UnitOfMeasurement) = UiUnitOfMeasurement(
            unit = domainModel.unit
        )
    }
}