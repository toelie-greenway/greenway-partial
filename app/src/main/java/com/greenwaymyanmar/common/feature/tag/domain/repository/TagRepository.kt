package com.greenwaymyanmar.common.feature.tag.domain.repository

import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import greenway_myanmar.org.vo.Listing

interface TagRepository {
    fun getTagsListing(
        categoryId: String,
        query: String?
    ): Listing<Tag>

    suspend fun saveThreadTagVoting(
        tagId: String,
        threadId: String,
        categoryId: String,
        previousVotedOptionId: String?
    ): Boolean

    suspend fun removeThreadTagVoting(
        tagVoteOptionId: String
    ): Boolean
}