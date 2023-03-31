package com.greenwaymyanmar.common.feature.tag.presentation.model

import com.greenwaymyanmar.common.feature.tag.domain.model.TagProduct

data class UiTagProduct(
    val id: String,
    val productName: String,
    val distributorName: String,
    val imageUrl: String?
) {
    companion object {
        fun fromDomain(domainModel: com.greenwaymyanmar.common.feature.tag.domain.model.TagProduct) = UiTagProduct(
            id = domainModel.id,
            productName = domainModel.productName,
            distributorName = domainModel.distributorName,
            imageUrl = domainModel.imageUrls.firstOrNull()
        )
    }
}