package greenway_myanmar.org.features.tag.domain.model

import kotlinx.datetime.Instant

data class TagPost(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String?,
    val createdAt: Instant
)