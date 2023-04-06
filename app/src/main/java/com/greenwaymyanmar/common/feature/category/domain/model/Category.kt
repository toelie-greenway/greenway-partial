package com.greenwaymyanmar.common.feature.category.domain.model

import com.greenwaymyanmar.vo.common.CategoryID

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String,
    val type: CategoryType,
    val parentCategoryId: String
)

enum class CategoryType(val key: String) {
    Unknown("") {
        override fun categoryId() = CategoryID.UNKNOWN
    },
    Agri("agri") {
        override fun categoryId() = CategoryID.ARGI
    },
    Livestock("livestock") {
        override fun categoryId() = CategoryID.LIVESTOCK
    },
    Aqua("aquaculture") {
        override fun categoryId() = CategoryID.AQUACULTURE
    };

    abstract fun categoryId(): CategoryID

    companion object {
        fun fromString(key: String) = when (key) {
            Agri.key -> Agri
            Livestock.key -> Livestock
            Aqua.key -> Aqua
            else -> Unknown
        }
    }
}