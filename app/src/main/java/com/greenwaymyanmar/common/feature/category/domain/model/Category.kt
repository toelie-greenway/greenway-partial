package com.greenwaymyanmar.common.feature.category.domain.model

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String,
    val type: CategoryType
)

enum class CategoryType {
    Unknown,
    Agri,
    Livestock,
    Aqua
}