package com.greenwaymyanmar.common.feature.tag.presentation.model

import android.os.Parcelable
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiTag(
    val id: String,
    val name: String,
    val category: UiCategory,
    val imageUrls: List<String>
) : Parcelable {
    companion object {
        fun fromDomainModel(domainModel: Tag) = UiTag(
            id = domainModel.id,
            name = domainModel.name,
            category = UiCategory.fromDomainModel(domainModel.category),
            imageUrls = domainModel.imageUrls
        )
    }
}