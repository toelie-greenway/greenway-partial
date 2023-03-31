package com.greenwaymyanmar.common.feature.tag.presentation.model

import com.greenwaymyanmar.common.feature.tag.domain.model.TagThread
import kotlinx.datetime.Instant

data class UiTagThread(
    val id: String,
    val question: String,
    val createdAt: Instant,
    val imageUrl: String?
) {
    companion object {
        fun fromDomain(domainModel: com.greenwaymyanmar.common.feature.tag.domain.model.TagThread) = UiTagThread(
            id = domainModel.id,
            question = domainModel.question,
            createdAt = domainModel.createdAt,
            imageUrl = domainModel.thumbnailImageUrl
        )
    }
}