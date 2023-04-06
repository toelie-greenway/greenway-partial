package com.greenwaymyanmar.common.feature.category.data.source.network.model

import com.greenwaymyanmar.common.feature.category.domain.model.Category
import com.greenwaymyanmar.common.feature.category.domain.model.CategoryType
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    val id: String? = "",
    val title: String? = "",
    val description: String? = "",
    val order: Int? = -1,
    val type: String? = null,
    val image: String? = null,
    val parent_id: String? = null
)

fun NetworkCategory.asDomainModel() =
    Category(
        id = id.orEmpty(),
        name = title.orEmpty(),
        imageUrl = image.orEmpty(),
        type = CategoryType.fromString(type.orEmpty()),
        parentCategoryId = parent_id.orEmpty()
    )