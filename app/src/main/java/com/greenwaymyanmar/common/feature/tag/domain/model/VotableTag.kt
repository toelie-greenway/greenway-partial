package com.greenwaymyanmar.common.feature.tag.domain.model

import com.greenwaymyanmar.common.feature.category.domain.model.Category

data class VotableTag(
    val id: String,
    val votesCount: Int,
    val isVoted: Boolean,
    val tag: Tag,
    val category: Category? = null
)