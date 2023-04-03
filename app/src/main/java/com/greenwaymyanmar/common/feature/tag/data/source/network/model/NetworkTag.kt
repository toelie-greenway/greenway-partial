package com.greenwaymyanmar.common.feature.tag.data.source.network.model

import com.greenwaymyanmar.common.feature.tag.domain.model.Tag

data class NetworkTag(
    val id: String? = null,
    val name: String? = null,
    val images: List<String>? = null
)

fun NetworkTag.toDomainModel() = Tag(
    id = id.orEmpty(),
    name = name.orEmpty(),
    category = null,
    imageUrls = images.orEmpty()
)