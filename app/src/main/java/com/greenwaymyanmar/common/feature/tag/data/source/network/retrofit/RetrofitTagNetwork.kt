package com.greenwaymyanmar.common.feature.tag.data.source.network.retrofit

import com.greenwaymyanmar.common.data.api.response.ApiDataResponse
import com.greenwaymyanmar.common.data.api.v3.ApiResponse
import com.greenwaymyanmar.common.data.api.v3.getDataOrThrow
import com.greenwaymyanmar.common.feature.tag.data.source.network.TagNetworkDataSource
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkStatusResponse
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkTagListResponse
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkVotableTag
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitTagNetworkApi {

    @GET("tags")
    fun getTags(
        @Query("q") query: String? = null,
        @Query("category_id") categoryId: String? = null,
        @Query("categories") categories: List<String>? = null,
        @Query("crops") crops: List<String>? = null,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<NetworkTagListResponse>

    @FormUrlEncoded
    @POST("tags/{tag_id}/vote")
    suspend fun postThreadTagVote(
        @Path("tag_id") tagId: String,
        @Field("thread_id") threadId: String,
        @Field("user_id") userId: String,
        @Field("categoryId") categoryId: String,
    ): ApiResponse<ApiDataResponse<NetworkVotableTag>>

    @DELETE("suggested-tags/{vote_option_id}/unvote")
    suspend fun deleteThreadTagVote(
        @Path("vote_option_id") tagVoteOptionId: String,
        @Query("user_id") userId: String,
    ): ApiResponse<NetworkStatusResponse>

}

@Singleton
class RetrofitTagNetworkDataSource @Inject constructor(retrofit: Retrofit) :
    TagNetworkDataSource {

    private val networkApi: RetrofitTagNetworkApi =
        retrofit.create(RetrofitTagNetworkApi::class.java)

    override fun getTagsCall(
        query: String?,
        categoryId: String?,
        categories: List<String>?,
        crops: List<String>?,
        page: Int,
        limit: Int
    ): Call<NetworkTagListResponse> = networkApi.getTags(
        query = query,
        categoryId = categoryId,
        categories = categories,
        crops = crops,
        page = page,
        limit = limit
    )

    override suspend fun postThreadTagVote(
        tagId: String,
        threadId: String,
        categoryId: String,
        userId: String
    ): NetworkVotableTag = networkApi.postThreadTagVote(
        tagId = tagId,
        threadId = threadId,
        categoryId = categoryId,
        userId = userId
    ).getDataOrThrow().data

    override suspend fun deleteThreadTagVote(
        tagVoteOptionId: String,
        userId: String
    ): NetworkStatusResponse =
        networkApi.deleteThreadTagVote(
            tagVoteOptionId = tagVoteOptionId,
            userId = userId
        ).getDataOrThrow()
}