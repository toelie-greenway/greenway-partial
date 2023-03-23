package greenway_myanmar.org.features.tag.presentation.model

import greenway_myanmar.org.features.tag.domain.model.TagPost
import kotlinx.datetime.Instant

data class UiTagPost(
    val id: String,
    val title: String,
    val author: String,
    val createdAt: Instant,
    val imageUrl: String?
) {
    companion object {
        fun fromDomain(domainModel: TagPost) = UiTagPost(
            id = domainModel.id,
            title = domainModel.title,
            author = domainModel.author,
            createdAt = domainModel.createdAt,
            imageUrl = domainModel.imageUrl
        )
    }
}