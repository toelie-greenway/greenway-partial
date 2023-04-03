package com.greenwaymyanmar.common.feature.category.domain.model

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String,
    val type: CategoryType
)

enum class CategoryType(val key: String) {
    Unknown(""),
    Agri("agri"),
    Livestock("livestock"),
    Aqua("fishery");

    companion object {
        fun fromString(key: String) = when (key) {
            Agri.key -> Agri
            Livestock.key -> Livestock
            Aqua.key -> Aqua
            else -> Unknown
        }
    }
}