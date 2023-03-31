package com.greenwaymyanmar.common.feature.tag.domain.model

import com.greenwaymyanmar.common.feature.category.domain.model.Category

data class Tag(
    val id: String,
    val name: String,
    val category: Category,
    val imageUrls: List<String>
)
