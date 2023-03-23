package greenway_myanmar.org.features.tag.domain.model

import kotlinx.datetime.Instant

data class TagThread(
    val id: String,
    val question: String,
    val imageUrls: List<String>,
    val createdAt: Instant
) {
    val thumbnailImageUrl: String? = imageUrls.firstOrNull()
}