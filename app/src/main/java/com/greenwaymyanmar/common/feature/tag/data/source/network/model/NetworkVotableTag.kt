package com.greenwaymyanmar.common.feature.tag.data.source.network.model

import com.greenwaymyanmar.common.feature.category.data.source.network.model.NetworkCategory
import com.greenwaymyanmar.common.feature.category.data.source.network.model.asDomainModel
import com.greenwaymyanmar.common.feature.tag.domain.model.VotableTag

@kotlinx.serialization.Serializable
data class NetworkVotableTag(
    val id: String,
    val votes_count: Int,
    val is_voted: Boolean,
    val tag: NetworkTag,
    val category: NetworkCategory? = null
)

fun NetworkVotableTag.asDomainModel() = VotableTag(
    id = id,
    votesCount = votes_count,
    isVoted = is_voted,
    tag = tag.asDomainModel(),
    category = category?.asDomainModel()
)