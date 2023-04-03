package com.greenwaymyanmar.common.feature.tag.data.source.network

import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkTagListResponse
import retrofit2.Call

interface TagNetworkDataSource {

    fun getTagsCall(
        categoryId: String? = null,
        categories: List<String>? = null,
        crops: List<String>? = null,
        page: Int,
        limit: Int
    ): Call<NetworkTagListResponse>

}