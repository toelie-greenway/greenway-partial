package greenway_myanmar.org.features.tag.presentation.model

import greenway_myanmar.org.features.tag.domain.model.TagThread
import kotlinx.datetime.Instant

data class UiTagThread(
    val id: String,
    val question: String,
    val createdAt: Instant,
    val imageUrl: String?
) {
    companion object {
        fun fromDomain(domainModel: TagThread) = UiTagThread(
            id = domainModel.id,
            question = domainModel.question,
            createdAt = domainModel.createdAt,
            imageUrl = domainModel.thumbnailImageUrl
        )
    }
}