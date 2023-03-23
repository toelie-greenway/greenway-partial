package greenway_myanmar.org.features.tag.presentation.model

import greenway_myanmar.org.features.tag.domain.model.Tag

data class UiTag(
    val id: String,
    val name: String,
    val categoryName: String,
    val imageUrls: List<String>
) {
    companion object {
        fun fromDomainModel(domainModel: Tag) = UiTag(
            id = domainModel.id,
            name = domainModel.name,
            categoryName = domainModel.category,
            imageUrls = domainModel.imageUrls
        )
    }
}