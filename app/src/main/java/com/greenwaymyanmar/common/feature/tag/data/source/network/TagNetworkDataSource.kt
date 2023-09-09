package com.greenwaymyanmar.common.feature.tag.data.source.network

import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkStatusResponse
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkTagListResponse
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkVotableTag
import retrofit2.Call

interface TagNetworkDataSource {

    fun getTagsCall(
        query: String? = null,
        categoryId: String? = null,
        categories: List<String>? = null,
        crops: List<String>? = null,
        page: Int,
        limit: Int
    ): Call<NetworkTagListResponse>

    suspend fun postThreadTagVote(
        tagId: String,
        threadId: String,
        categoryId: String,
        userId: String
    ): NetworkVotableTag

    suspend fun deleteThreadTagVote(
        tagVoteOptionId: String,
        userId: String
    ): NetworkStatusResponse
}