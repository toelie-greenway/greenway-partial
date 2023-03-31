package com.greenwaymyanmar.common.feature.tag.presentation.model

import com.greenwaymyanmar.common.feature.tag.domain.model.TagPost
import kotlinx.datetime.Instant

data class UiTagPost(
    val id: String,
    val title: String,
    val author: String,
    val createdAt: Instant,
    val imageUrl: String?
) {
    companion object {
        fun fromDomain(domainModel: com.greenwaymyanmar.common.feature.tag.domain.model.TagPost) = UiTagPost(
            id = domainModel.id,
            title = domainModel.title,
            author = domainModel.author,
            createdAt = domainModel.createdAt,
            imageUrl = domainModel.imageUrl
        )
    }
}