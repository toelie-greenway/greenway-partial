package com.greenwaymyanmar.common.feature.tag.data.source.network.retrofit

import com.greenwaymyanmar.common.feature.tag.data.source.network.TagNetworkDataSource
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkTagListResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitTagNetworkApi {

    @GET("tags")
    fun getTags(
        @Query("category_id") categoryId: String? = null,
        @Query("categories") categories: List<String>? = null,
        @Query("crops") crops: List<String>? = null,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<NetworkTagListResponse>

}

@Singleton
class RetrofitTagNetworkDataSource @Inject constructor(retrofit: Retrofit) :
    TagNetworkDataSource {

    private val networkApi: RetrofitTagNetworkApi =
        retrofit.create(RetrofitTagNetworkApi::class.java)

    override fun getTagsCall(
        categoryId: String?,
        categories: List<String>?,
        crops: List<String>?,
        page: Int,
        limit: Int
    ): Call<NetworkTagListResponse> = networkApi.getTags(
        categoryId = categoryId,
        categories = categories,
        crops = crops,
        page = page,
        limit = limit
    )


}