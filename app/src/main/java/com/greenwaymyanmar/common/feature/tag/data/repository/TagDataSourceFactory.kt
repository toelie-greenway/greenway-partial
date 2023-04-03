package com.greenwaymyanmar.common.feature.tag.data.repository

import com.google.gson.Gson
import com.greenwaymyanmar.common.feature.tag.data.source.network.TagNetworkDataSource
import com.greenwaymyanmar.common.feature.tag.data.source.network.model.NetworkTagListResponse
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.repository.paging.datasource.BaseDataSourceFactory

class TagDataSourceFactory(
    private val network: TagNetworkDataSource,
    private val appExecutors: AppExecutors,
    private val gson: Gson,
    private val networkPageSize: Int
) : BaseDataSourceFactory<NetworkTagListResponse, Tag, TagPageKeyedDataSource>() {

    override fun createDataSource(): TagPageKeyedDataSource {
        return TagPageKeyedDataSource(
            network, appExecutors, gson, networkPageSize
        )
    }
}
