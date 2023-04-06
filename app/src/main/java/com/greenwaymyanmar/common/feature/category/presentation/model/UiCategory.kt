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

        fun fromCategoryVo(category: greenway_myanmar.org.vo.Category) = UiCategory(
            id = category.id,
            name = category.title,
            imageUrl = category.image,
            type = UiCategoryType.fromDomainModel(CategoryType.fromString(category.type))
        )
    }
}

enum class UiCategoryType {
    Unknown {
        override fun toDomainModel() = CategoryType.Unknown
    },
    Agri {
        override fun toDomainModel() = CategoryType.Agri
    },
    Livestock {
        override fun toDomainModel() = CategoryType.Livestock
    },
    Aqua {
        override fun toDomainModel() = CategoryType.Aqua
    };

    abstract fun toDomainModel(): CategoryType

    companion object {
        fun fromDomainModel(domainModel: CategoryType) = when (domainModel) {
            CategoryType.Agri -> Agri
            CategoryType.Livestock -> Livestock
            CategoryType.Aqua -> Aqua
            CategoryType.Unknown -> Unknown
        }
    }
}