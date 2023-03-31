package com.greenwaymyanmar.common.feature.category.presentation.model

import android.os.Parcelable
import com.greenwaymyanmar.common.feature.category.domain.model.Category
import com.greenwaymyanmar.common.feature.category.domain.model.CategoryType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiCategory(
    val id: String,
    val name: String,
    val imageUrl: String,
    val type: UiCategoryType
) : Parcelable {
    companion object {
        fun fromDomainModel(domainModel: Category) = UiCategory(
            id = domainModel.id,
            name = domainModel.name,
            imageUrl = domainModel.imageUrl,
            type = UiCategoryType.fromDomainModel(domainModel.type)
        )
    }
}

enum class UiCategoryType {
    Unknown,
    Agri,
    Livestock,
    Aqua;

    companion object {
        fun fromDomainModel(domainModel: CategoryType) = when (domainModel) {
            CategoryType.Agri -> Agri
            CategoryType.Livestock -> Livestock
            CategoryType.Aqua -> Aqua
            CategoryType.Unknown -> Unknown
        }
    }
}