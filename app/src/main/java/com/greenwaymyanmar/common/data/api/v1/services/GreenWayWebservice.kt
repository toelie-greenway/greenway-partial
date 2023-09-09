package com.greenwaymyanmar.common.data.api.v1.services

import com.greenwaymyanmar.common.data.api.v1.response.PostListResponse
import com.greenwaymyanmar.common.data.api.v1.response.ProductListResponse
import com.greenwaymyanmar.common.data.api.v1.response.ThreadListResponse
import com.greenwaymyanmar.common.data.api.v3.ApiResponse
import com.greenwaymyanmar.common.feature.category.data.source.network.model.NetworkCategory
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GreenWayWebservice {

    @GET("api_path")
    fun getSomething(): ApiResponse<String>

    @GET("threads")
    fun getThreads(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("user_id") userId: Int,
        @QueryMap filter: Map<String, @JvmSuppressWildcards String?>
    ): Call<ThreadListResponse>

    @GET("marketplace/main-products")
    fun getMainProducts(
        @Query("filter") query: String?,
        @Query("tags") tags: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<ProductListResponse>

    @GET("categories/12/posts")
    fun getPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<PostListResponse>

    @GET("categories")
    suspend fun getCategories(
        @Query("filter") filter: String? = null
    ): ApiResponse<List<NetworkCategory>>

}
