package com.greenwaymyanmar.common.feature.tag.data.source.network.model

import com.greenwaymyanmar.common.feature.category.data.source.network.model.NetworkCategory
import com.greenwaymyanmar.common.feature.category.data.source.network.model.asDomainModel
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag

@kotlinx.serialization.Serializable
data class NetworkTag(
    val id: String? = null,
    val name: String? = null,
    val category: NetworkCategory? = null,
    val categories: List<NetworkCategory>? = null,
    val images: List<String>? = null
)

fun NetworkTag.asDomainModel() = Tag(
    id = id.orEmpty(),
    name = name.orEmpty(),
    category = category?.asDomainModel(),
    categories = categories.orEmpty().map { it.asDomainModel() },
    imageUrls = images.orEmpty()
)