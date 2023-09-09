package com.greenwaymyanmar.common.feature.tag.data.repository

import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v1.Pagination
import com.greenwaymyanmar.common.feature.tag.data.source.network.TagNetworkDataSource
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkTagListResponse
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.asDomainModel
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.toPaginationModel
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.repository.paging.datasource.BasePageKeyedGreenWayDataSource
import retrofit2.Call

class TagPageKeyedDataSource constructor(
    private val categoryId: String,
    private val query: String?,
    private val network: TagNetworkDataSource,
    appExecutors: AppExecutors,
    gson: Gson,
    private val networkPageSize: Int
) : BasePageKeyedGreenWayDataSource<NetworkTagListResponse, Tag>(
    appExecutors, gson
) {
    override fun createCall(): Call<NetworkTagListResponse> {
        return network.getTagsCall(
            query = query,
            categoryId = categoryId,
            categories = null,
            crops = null,
            page = 1,
            limit = networkPageSize
        )
    }

    override fun createNextPageCall(nextPage: Int): Call<NetworkTagListResponse> {
        return network.getTagsCall(
            query = query,
            categoryId = categoryId,
            categories = null,
            crops = null,
            page = nextPage,
            limit = networkPageSize
        )
    }

    override fun getPaginationFromResponseBody(requestType: NetworkTagListResponse?): Pagination? {
        return requestType?.meta?.toPaginationModel()
    }

    override fun getItemsFromResponse(requestType: NetworkTagListResponse?): List<Tag> {
        return requestType?.data.orEmpty().map {
            it.asDomainModel()
        }
    }
}