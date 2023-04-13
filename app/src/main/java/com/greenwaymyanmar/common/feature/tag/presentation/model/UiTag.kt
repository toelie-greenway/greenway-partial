package com.greenwaymyanmar.common.feature.tag.presentation.model

import android.graphics.Color
import android.os.Parcelable
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiTag(
    val id: String,
    val name: String,
    val category: UiCategory?,
    val categories: List<UiCategory>?,
    val imageUrls: List<String>,
    val color: Int
) : Parcelable {
    companion object {
        fun fromDomainModel(domainModel: Tag, index: Int = 0) = UiTag(
            id = domainModel.id,
            name = domainModel.name,
            category = if (domainModel.category != null) {
                UiCategory.fromDomainModel(domainModel.category)
            } else {
                null
            },
            categories = domainModel.categories.orEmpty().map { UiCategory.fromDomainModel(it) },
            imageUrls = domainModel.imageUrls,
            color = getTagColor(index)
        )
    }
}

fun getTagColor(index: Int) = TAG_COLORS.getOrElse(index) { Color.parseColor("#000000") }

val TAG_COLORS = listOf(
    Color.parseColor("#FF0097"),
    Color.parseColor("#007AFF"),
    Color.parseColor("#009789"),
    Color.parseColor("#F5A623"),
    Color.parseColor("#FF4081"),
    Color.parseColor("#000000"),
)