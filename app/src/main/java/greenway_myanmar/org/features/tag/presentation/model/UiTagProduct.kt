package greenway_myanmar.org.features.tag.presentation.model

import greenway_myanmar.org.features.tag.domain.model.TagProduct

data class UiTagProduct(
    val id: String,
    val productName: String,
    val distributorName: String,
    val imageUrl: String?
) {
    companion object {
        fun fromDomain(domainModel: TagProduct) = UiTagProduct(
            id = domainModel.id,
            productName = domainModel.productName,
            distributorName = domainModel.distributorName,
            imageUrl = domainModel.imageUrls.firstOrNull()
        )
    }
}